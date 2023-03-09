import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit'
import { API_URL, authApi, LoginData, userApi } from '../http/axios'
import jwt_decode from "jwt-decode";
import axios, { AxiosError } from 'axios';
import { clearCartState, getCartAfterLogin } from './cartReducer';
import { setLoading } from './adminReducer';

type AuthState = {
    id: number | null,
    username: string | null,
    surname: string | null,
    email: string | null,
    role: 'ROLE_USER' | 'ROLE_ADMIN' | null,
    isAuth: boolean,
    loading: boolean,
    loginError: string | null,
    registrationError: string | null,
    registrationSuccess: boolean,
    verificationSuccess: boolean | null,
    verificationErrorMessage: string
}

type AccessTokenDecoded = {
    id: number,
    name: string,
    surname: string,
    email: string,
    role: 'ROLE_USER' | 'ROLE_ADMIN',
}


const initialState: AuthState = {
    id: null,
    username: null,
    surname: null,
    email: null,
    role: null,
    isAuth: false,
    loading: false,
    loginError: null,
    registrationError: null,
    registrationSuccess: false,
    verificationSuccess: null,
    verificationErrorMessage: ''
}

export const fetchRegistration = createAsyncThunk<void, FormData>('auth/fetchRegistration',
    async (data, { dispatch }) => {
        try {
            await authApi.registration(data)
            dispatch(setRegistrationError(null))
            dispatch(setRegistrationSuccess(true))
        } catch (err: any | unknown) {
            let error: AxiosError<{ message: string, timestamp: number }> = err
            if (error.response?.data.message) {
                dispatch(setRegistrationError(error.response?.data.message))
            }
            dispatch(setLoading(false))
        }

    })

export const fetchLogin = createAsyncThunk<void, LoginData>('auth/fetchLogin',
    async (data, { dispatch }) => {
        try {
            const response = await authApi.login(data.email, data.password)
            localStorage.setItem('token', response.data.accessToken)
            let userData = jwt_decode<AccessTokenDecoded>(response.data.accessToken)
            dispatch(setUsername(userData.name))
            dispatch(setSurName(userData.surname))
            dispatch(setEmail(userData.email))
            dispatch(setRole(userData.role))
            dispatch(setUserId(userData.id))
            dispatch(setIsAuth(true))
            dispatch(setLoginError(null))
            dispatch(getCartAfterLogin())
        } catch (err: any | unknown) {
            let error: AxiosError<{ message: string, timestamp: number }> = err
            if (error.response?.data.message) {
                dispatch(setLoginError(error.response?.data.message))
            }
            dispatch(setLoading(false))
        }
    })
export const fetchLogout = createAsyncThunk('auth/fetchLogout',
    async (_, { dispatch }) => {
        try {
            await authApi.logout()
            localStorage.removeItem('token')
            dispatch(setUsername(null))
            dispatch(setSurName(null))
            dispatch(setEmail(null))
            dispatch(setUserId(null))
            dispatch(setRole(null))
            dispatch(setIsAuth(false))
            dispatch(clearCartState())
        } catch (error) {
            console.log(error)
        }
    })
export const checkAuth = createAsyncThunk('auth/checkAuth',
    async (_, { dispatch }) => {
        try {
            const response = await axios.get(`${API_URL}auth/getnewaccesstoken`, { withCredentials: true })
            localStorage.setItem('token', response.data.accessToken)
            let userData = jwt_decode<AccessTokenDecoded>(response.data.accessToken)
            dispatch(setUsername(userData.name))
            dispatch(setSurName(userData.surname))
            dispatch(setEmail(userData.email))
            dispatch(setRole(userData.role))
            dispatch(setUserId(userData.id))
            dispatch(setIsAuth(true))
            dispatch(getCartAfterLogin())
        } catch (error: any) {
            console.log(error?.response)
        }
    })

export const fetchVerification = createAsyncThunk<void, string>('auth/fetchVerification',
    async (key, { dispatch }) => {
        try {
            dispatch(setLoading(true))
            await authApi.verification(key)
            dispatch(setVerificationSuccess(true))
            dispatch(setLoading(false))
        } catch (err: any | unknown) {
            let error: AxiosError<{ message: string, timestamp: number }> = err
            if (error.response?.data.message) {
                dispatch(setVerificationErrorMessage(error.response?.data.message))
            }
            dispatch(setVerificationSuccess(false))
            dispatch(setLoading(false))
        }
    })
export const fetchUpdateProfile = createAsyncThunk<void, FormData>('auth/updateProfile',
    async (data, { dispatch }) => {
        try {
            dispatch(setLoading(true))
            await userApi.updateProfile(data)
        } catch (err: any | unknown) {
            let error: AxiosError<{ message: string, timestamp: number }> = err
            console.log(error)
            dispatch(setLoading(false))
        }
    })


export const authSLice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        setIsAuth(state, action: PayloadAction<boolean>) {
            state.isAuth = action.payload
        },
        setUserId(state, action: PayloadAction<number | null>) {
            state.id = action.payload
        },
        setUsername(state, action: PayloadAction<string | null>) {
            state.username = action.payload
        },
        setSurName(state, action: PayloadAction<string | null>) {
            state.surname = action.payload
        },
        setEmail(state, action: PayloadAction<string | null>) {
            state.email = action.payload
        },
        setRole(state, action: PayloadAction<'ROLE_USER' | 'ROLE_ADMIN' | null>) {
            state.role = action.payload
        },
        setLoginError(state, action: PayloadAction<string | null>) {
            state.loginError = action.payload
        },
        setRegistrationError(state, action: PayloadAction<string | null>) {
            state.registrationError = action.payload
        },
        setRegistrationSuccess(state, action: PayloadAction<boolean>) {
            state.registrationSuccess = action.payload
        },
        setVerificationSuccess(state, action: PayloadAction<boolean>) {
            state.verificationSuccess = action.payload
        },
        setVerificationErrorMessage(state, action: PayloadAction<string>) {
            state.verificationErrorMessage = action.payload
        }
    },
    extraReducers: builder => {
        builder.addCase(fetchLogin.pending, state => {
            state.loading = true
        })
        builder.addCase(fetchLogin.fulfilled, state => {
            state.loading = false
        })
        builder.addCase(fetchRegistration.pending, state => {
            state.loading = true
        })
        builder.addCase(fetchRegistration.fulfilled, state => {
            state.loading = false
        })
        builder.addCase(fetchLogout.pending, state => {
            state.loading = true
        })
        builder.addCase(fetchLogout.fulfilled, state => {
            state.loading = false
        })
    }
})


export default authSLice.reducer
export const {
    setIsAuth,
    setUserId,
    setUsername,
    setSurName,
    setEmail,
    setRole,
    setLoginError,
    setRegistrationError,
    setRegistrationSuccess,
    setVerificationErrorMessage,
    setVerificationSuccess } = authSLice.actions