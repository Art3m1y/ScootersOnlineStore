import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { Scooter } from './scootersCatalogReducer'



type FavoritesState = {
    favoritesItems: Scooter[]
}

let initialState: FavoritesState = {
    favoritesItems: []
}

export const favoritesReducer = createSlice({
    name: 'favorites',
    initialState,
    reducers: {
        addItemToFavorites(state, action: PayloadAction<Scooter>) {
            let index = state.favoritesItems.findIndex(item => item.id === action.payload.id)
            if (index !== -1) {
                state.favoritesItems.splice(index, 1)
            } else {
                state.favoritesItems.push(action.payload)
            }
            localStorage.setItem('scootersFavorites', JSON.stringify(state))
        },
        getFavoritesFromLocalStorage(state) {
            let favoritesData = localStorage.getItem('scootersFavorites')
            if (favoritesData !== null) {
                let data: FavoritesState = JSON.parse(favoritesData)
                state.favoritesItems = data.favoritesItems
            }
        }

    }
})

export default favoritesReducer.reducer
export const { addItemToFavorites, getFavoritesFromLocalStorage } = favoritesReducer.actions