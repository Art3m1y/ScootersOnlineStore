import { createSlice } from '@reduxjs/toolkit'



type App = {
    theme: 'light' | 'dark'
}

let initialState: App = {
    theme: 'light'
}

export const appReducer = createSlice({
    name: 'cart',
    initialState,
    reducers: {
        toggleTheme(state) {
            state.theme === 'light' ? state.theme = 'dark' : state.theme = 'light'
            localStorage.setItem('theme', state.theme)
        },
        getThemeFromStorage(state) {
            let themeFromStorage = localStorage.getItem('theme')
            if (themeFromStorage === 'light' || themeFromStorage === 'dark') {
                state.theme = themeFromStorage
            }
        }
    }
})

export default appReducer.reducer
export const { toggleTheme, getThemeFromStorage } = appReducer.actions