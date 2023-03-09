import React, { useEffect, lazy, Suspense } from 'react';
import { BrowserRouter as Router, Routes, Route, } from "react-router-dom";
import CssBaseline from '@mui/material/CssBaseline';
import { checkAuth } from './redux/authReducer';
import "@fontsource/jost/400.css"
import "@fontsource/jost/500.css"
import "@fontsource/jost/600.css"
import "@fontsource/jost/700.css"

import { useAppDispatch, useAppSelector } from './redux/hooks';
import { HeaderMUI } from './components/header/HeaderMUI';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { ScooterPage } from './components/scooterPage/ScooterPage';
import { getCartAfterLogin, getCartFromLocalStorage } from './redux/cartReducer';
import { getFavoritesFromLocalStorage } from './redux/favoritesReducer';
import { getThemeFromStorage } from './redux/appReducer';
import { Analytics } from '@vercel/analytics/react';


const NotFound = lazy(() => import("./components/notFound/NotFound"));
const LoginFormMUI = lazy(() => import("./components/loginForm/LoginFormMUI"));
const RegistrationFormMUI = lazy(() => import("./components/registrationForm/RegistrationFormMUI"));
const Cart = lazy(() => import("./components/cart/Cart"));
const Admin = lazy(() => import("./components/admin/admin"));
const Catalog = lazy(() => import("./components/catalog/Catalog"));
const Favorites = lazy(() => import("./components/favorites/Favorites"));
const Verification = lazy(() => import("./components/verificationPage/verification"));
const Profile = lazy(() => import("./components/profile/Profile"));



const App: React.FC = () => {
  const mode = useAppSelector(store => store.app.theme)
  const theme = createTheme({
    palette: {
      mode: mode,
      primary: {
        main: '#6F73EE',
      },
      secondary: { main: '#fff' }
    },
    typography: {
      fontFamily: "'Jost', sans-serif"
    }
  });

  const dispatch = useAppDispatch()

  useEffect(() => {
    if (localStorage.getItem('token')) {
      dispatch(checkAuth())
      dispatch(getCartAfterLogin())
    } else {
      dispatch(getCartFromLocalStorage())
    }
    dispatch(getFavoritesFromLocalStorage())
    dispatch(getThemeFromStorage())
  }, [dispatch])


  return (

    <div className="App">
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Router>
          <HeaderMUI />
          <Suspense>
            <Routes>
              <Route path='/' element={<Catalog />} />
              <Route path='/login' element={<LoginFormMUI />} />
              <Route path='/registration' element={<RegistrationFormMUI />} />
              <Route path='/cart' element={<Cart />} />
              <Route path='/admin' element={<Admin />} />
              <Route path='/catalog/:id' element={<ScooterPage />} />
              <Route path='/favorites' element={<Favorites />} />
              <Route path='/verification/:key' element={<Verification />} />
              <Route path='/profile' element={<Profile />} />

              <Route path='*' element={<NotFound />} />
            </Routes>
          </Suspense>
        </Router>
      </ThemeProvider>
      <Analytics />
    </div>


  );
}

export default App;
