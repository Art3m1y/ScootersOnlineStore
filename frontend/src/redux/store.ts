import { combineReducers, configureStore } from "@reduxjs/toolkit";
import authReducer from "./authReducer";
import cartReducer from "./cartReducer";
import scootersCatalogReducer from "./scootersCatalogReducer";
import favoritesReducer from "./favoritesReducer";
import appReducer from "./appReducer";
import adminReducer from "./adminReducer";


const rootReducer = combineReducers({
    app: appReducer,
    auth: authReducer,
    cart: cartReducer,
    catalog: scootersCatalogReducer,
    favorites: favoritesReducer,
    admin: adminReducer
})

const store = configureStore({
    reducer: rootReducer,
})


export default store;

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch