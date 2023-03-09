import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit'
import { AddCommentData, catalogApi } from '../http/axios'


type Images = {
    link: string
}
export type Person = {
    name: string,
    surname: string,
    id: number
}
export interface CommentType {
    person: Person,
    text: string,
    mark: number,
    id: number
}
export interface Scooter {
    id: number
    name: string,
    cost: string,
    batteryCapacity: string,
    power: number,
    speed: string,
    time: string,
    images: Images[],
    mark: number,
    description?: string,
    comments?: CommentType[]
}

type ScooterState = {
    scooters: Scooter[],
    loading: boolean,
    query: string,
    pageNumber: number,
    totalItems: number
}

let initialState: ScooterState = {
    scooters: [],
    loading: true,
    query: '',
    pageNumber: 1,
    totalItems: 0
}

type GetScootersData = {
    page: number,
    query: string
}

export const getScooters = createAsyncThunk<void, GetScootersData>('scootersCatalog/getScooters',
    async (data, { dispatch }) => {
        const response = await catalogApi.getScooters(data.page, data.query)
        dispatch(setScootersBase(response.data))
    })

export const getOneScooterData = createAsyncThunk<void, number>('scootersCatalog/getOneScooterData',
    async (data, { dispatch }) => {
        const response = await catalogApi.getOneScooter(data)
        dispatch(setOneScooter(response.data))
    })

export const addComment = createAsyncThunk<void, AddCommentData>('scootersCatalog/addComment',
    async (data, { dispatch }) => {
        await catalogApi.addComment(data)
        dispatch(getOneScooterData(data.id))
    })
export const deleteComment = createAsyncThunk<void, { commentId: number, productId: number }>('scootersCatalog/deleteComment',
    async (data, { dispatch }) => {
        await catalogApi.deleteComment(data.commentId)
        dispatch(getOneScooterData(data.productId))
    })

export const scootersCatalogReducer = createSlice({
    name: 'scootersCatalog',
    initialState,
    reducers: {
        setScootersBase(state, action: PayloadAction<{ amount: number, products: Scooter[] }>) {
            state.scooters = [...action.payload.products]
            state.totalItems = action.payload.amount
        },
        setOneScooter(state, action: PayloadAction<Scooter>) {
            state.scooters = [action.payload]
        },
        setPage(state, action: PayloadAction<number>) {
            state.pageNumber = action.payload
        },
        setQuery(state, action: PayloadAction<string>) {
            state.query = action.payload
        }
    },
    extraReducers: builder => {
        builder.addCase(getScooters.pending, state => {
            state.loading = true
        })
        builder.addCase(getScooters.fulfilled, state => {
            state.loading = false
        })
        builder.addCase(getOneScooterData.pending, state => {
            state.loading = true
        })
        builder.addCase(getOneScooterData.fulfilled, state => {
            state.loading = false
        })
    }
})

export default scootersCatalogReducer.reducer
export const { setScootersBase, setPage, setQuery, setOneScooter } = scootersCatalogReducer.actions