import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit'
import { adminApi, EditScooterData } from '../http/axios'
import { Scooter } from './scootersCatalogReducer'


type AdminData = {
    loading: boolean
    scooters: Scooter[]
}

let initialState: AdminData = {
    loading: false,
    scooters: []
}

export const fetchAddNewItem = createAsyncThunk<void, any>('admin/fetchAddNewItem',
    async (data) => {
        await adminApi.addNewItem(data)
    })

export const getScootersForAdmin = createAsyncThunk<void, void>('admin/getScootersForAdmin',
    async (_, { dispatch }) => {
        dispatch(setLoading(true))
        const response = await adminApi.getScooters()
        dispatch(setScooters(response.data))
        dispatch(setLoading(false))
    }
)
export const deleteScooterFromAdmin = createAsyncThunk<void, number>('admin/getScootersForAdmin',
    async (id, { dispatch }) => {
        dispatch(setLoading(true))
        await adminApi.deleteScooter(id)
        dispatch(getScootersForAdmin())
        dispatch(setLoading(false))
    }
)
export const editScooter = createAsyncThunk<void, EditScooterData>('admin/editScooter ',
    async (data, { dispatch }) => {
        dispatch(setLoading(true))
        await adminApi.editScooter(data)
        dispatch(getScootersForAdmin())
        dispatch(setLoading(false))
    }
)

export const appReducer = createSlice({
    name: 'admin',
    initialState,
    reducers: {
        setLoading(state, action: PayloadAction<boolean>) {
            state.loading = action.payload
        },
        setScooters(state, action: PayloadAction<{ amount: number, products: Scooter[] }>) {
            state.scooters = [...action.payload.products]
        },
    },
    extraReducers: builder => {
        builder.addCase(fetchAddNewItem.pending, state => {
            state.loading = true
        })
        builder.addCase(fetchAddNewItem.fulfilled, state => {
            state.loading = false
        })
    }
})

export default appReducer.reducer
export const { setLoading, setScooters } = appReducer.actions