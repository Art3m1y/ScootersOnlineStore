import axios, { AxiosResponse } from "axios";
import { ScooterForCart } from "../redux/cartReducer";
import { Scooter } from "../redux/scootersCatalogReducer";

export const API_URL = process.env.REACT_APP_API_URL

export type LoginData = {
    email: string,
    password: string,
}

export type UserDataForRegistration = {
    name: string,
    surname: string,
    yearOfBirth: number,
    email: string,
    country: string,
    password: string,
}

export type FetchRegistrationData = {
    register: UserDataForRegistration
    avatar: string
}

export type AddCommentData = {
    id: number,
    mark: number,
    text: string
}

type AuthResponse = {
    accessToken: string;
}

export type AddOneItemToCartData = {
    productId: number,
    amount: number
}

export type DeleteOneItemFromCartData = {
    productId: number,
    isAll: boolean
}
export type EditScooterData = {
    id: number
    name: string,
    cost: number,
    batteryCapacity: number,
    power: number,
    speed: number,
    time: number,
}

axios.defaults.withCredentials = true

const apiInstance = axios.create({
    withCredentials: true,
    baseURL: API_URL,

})

apiInstance.interceptors.request.use((config) => {
    if (config.headers) {
        config.headers.Authorization = `Bearer ${localStorage.getItem('token')}`
    }
    return config;
})

apiInstance.interceptors.response.use((config) => {
    return config;
}, async (error) => {
    try {
        const originalRequest = error.config;
        if (error.response.status === 401 && error.config && !error.config._isRetry) {
            originalRequest._isRetry = true;
            const response = await axios.get(`${API_URL}auth/getnewaccesstoken`, { withCredentials: true });
            localStorage.setItem('token', response.data.accessToken);
            return apiInstance.request(originalRequest);
        }
    } catch (error: any) {
        console.log(error);
    }
    throw error;
})



export const authApi = {
    async login(email: string, password: string): Promise<AxiosResponse<AuthResponse>> {
        return apiInstance.post<AuthResponse>('auth/login', { email, password })
    },
    async logout(): Promise<void> {
        return apiInstance.get('/auth/logout')
    },
    async registration(data: FormData): Promise<AxiosResponse<AuthResponse>> {
        return apiInstance.post<AuthResponse>('auth/registration', data)
    },
    async verification(key: string): Promise<void> {
        return apiInstance.get(`auth/activateAccount?code=${key}`)
    },

}



export const catalogApi = {
    async getScooters(page: number, query: string = ''): Promise<AxiosResponse<{ amount: number, products: Scooter[] }>> {
        return apiInstance.get<{ amount: number, products: Scooter[] }>(`catalog?search=${query}&page=${page}&itemsPerPage=10`)
    },
    async getOneScooter(id: number): Promise<AxiosResponse<Scooter>> {
        return apiInstance.get<Scooter>(`catalog/${id}`)
    },
    async addComment(data: AddCommentData): Promise<AxiosResponse<void>> {
        return apiInstance.post<void>(`comment/add`, { ...data })
    },
    async deleteComment(id: number): Promise<void> {
        return apiInstance.delete('comment/delete', { data: id })
    }
}

export const adminApi = {
    async addNewItem(data: any): Promise<void> {
        return apiInstance.post('admin/product/add', data)
    },
    async getScooters(): Promise<AxiosResponse<{ amount: number, products: Scooter[] }>> {
        return apiInstance.get<{ amount: number, products: Scooter[] }>(`catalog`)
    },
    async deleteScooter(id: number): Promise<void> {
        return apiInstance.delete('admin/product/delete', { data: id })
    },
    async editScooter(data: EditScooterData): Promise<void> {
        return apiInstance.patch('admin/product/update', data)
    }
}



export const cartApi = {
    async getCart(): Promise<AxiosResponse<ScooterForCart[]>> {
        return apiInstance.get('cart/get')
    },
    async addOneItem(data: AddOneItemToCartData) {
        return apiInstance.post('cart/add', data)
    },
    async deleteOneItem(deletingData: DeleteOneItemFromCartData) {
        return apiInstance.delete('cart/delete', { data: deletingData })
    },
    async clearCartOnServer() {
        return apiInstance.delete('cart/deleteCart')
    }
}

export const userApi = {
    async updateProfile(data: FormData): Promise<void> {
        return apiInstance.patch('/person/update', data)
    },
}