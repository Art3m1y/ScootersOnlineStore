import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit'
import { Scooter } from './scootersCatalogReducer'
import { AddOneItemToCartData, cartApi, DeleteOneItemFromCartData } from '../http/axios'



export interface ScooterForCart {
    product: Scooter
    amount: number
}

type CartState = {
    totalCost: number
    totalNumber: number
    cartItems: ScooterForCart[]
}

let initialState: CartState = {
    totalCost: 0,
    totalNumber: 0,
    cartItems: []
}


export const getCartFromServer = createAsyncThunk<void, void>('cart/getCartFromServer',
    async (_, { dispatch }) => {
        const response = await cartApi.getCart()
        dispatch(setCartFromServer(response.data))
    })
export const getCartAfterLogin = createAsyncThunk<void, void>('cart/getCartFromServer',
    async (_, { dispatch }) => {
        let cartData = localStorage.getItem('scootersCart')
        if (cartData !== null) {
            let data: CartState = JSON.parse(cartData)
            data.cartItems.forEach(item =>
                dispatch(addOneItemToCart({ productId: item.product.id, amount: item.amount }))
            )
            dispatch(clearCartState())
        }
        dispatch(getCartFromServer())
    })
export const addOneItemToCart = createAsyncThunk<void, AddOneItemToCartData>('cart/addOneItemToCart',
    async (data, { dispatch }) => {
        await cartApi.addOneItem(data)
        dispatch(getCartFromServer())
    })
export const deleteItemFromCart = createAsyncThunk<void, DeleteOneItemFromCartData>('cart/deleteItemFromCart',
    async (data, { dispatch }) => {
        await cartApi.deleteOneItem(data)
        dispatch(getCartFromServer())
    })
export const clearCartOnServer = createAsyncThunk<void, void>('cart/clearCartOnServer',
    async (_, { dispatch }) => {
        await cartApi.clearCartOnServer()
        dispatch(getCartFromServer())
    })




export const cartReducer = createSlice({
    name: 'cart',
    initialState,
    reducers: {
        addItemToCartLocal(state, action: PayloadAction<ScooterForCart>) {
            let index = state.cartItems.findIndex(item => item.product.id === action.payload.product.id)
            if (index !== -1) {
                state.cartItems[index].amount = state.cartItems[index].amount + 1
            } else {
                state.cartItems.push(action.payload)
            }
            state.totalNumber = state.totalNumber + 1
            state.totalCost = state.totalCost + +action.payload.product.cost
            localStorage.setItem('scootersCart', JSON.stringify(state))
        },
        reduceItemCountInCartLocal(state, action: PayloadAction<ScooterForCart>) {
            let index = state.cartItems.findIndex(item => item.product.id === action.payload.product.id)
            if (state.cartItems[index].amount > 1) {
                state.cartItems[index].amount = state.cartItems[index].amount - 1
            }
            state.totalNumber = state.totalNumber - 1
            state.totalCost = state.totalCost - +action.payload.product.cost
            localStorage.setItem('scootersCart', JSON.stringify(state))
        },
        deleteItemFromCartLocal(state, action: PayloadAction<{ id: number, cost: string, number: number }>) {
            let index = state.cartItems.findIndex(item => item.product.id === action.payload.id)
            state.cartItems.splice(index, 1)
            state.totalCost = state.totalCost - (+action.payload.cost * action.payload.number)
            state.totalNumber = state.totalNumber - action.payload.number
            localStorage.setItem('scootersCart', JSON.stringify(state))
        },
        getCartFromLocalStorage(state) {
            let cartData = localStorage.getItem('scootersCart')
            if (cartData !== null) {
                let data: CartState = JSON.parse(cartData)
                state.cartItems = data.cartItems
                state.totalCost = data.totalCost
                state.totalNumber = data.totalNumber
            }
        },
        clearCartState(state) {
            state.cartItems = []
            state.totalCost = 0
            state.totalNumber = 0
            localStorage.removeItem('scootersCart')
        },
        setCartFromServer(state, action: PayloadAction<ScooterForCart[]>) {
            state.cartItems = action.payload
            state.totalNumber = action.payload.reduce((acc, item) => acc + item.amount, 0)
            state.totalCost = action.payload.reduce((acc, item) => acc + (item.amount * +item.product.cost), 0)
        },


    }
})

export default cartReducer.reducer
export const {
    addItemToCartLocal,
    reduceItemCountInCartLocal,
    deleteItemFromCartLocal,
    getCartFromLocalStorage,
    clearCartState,
    setCartFromServer
} = cartReducer.actions