import React, { useEffect, lazy, Suspense } from 'react';
import { BrowserRouter as Router, Routes, Route, } from "react-router-dom";
import { checkAuth } from './redux/authReducer';
import "@fontsource/jost/400.css"
import "@fontsource/jost/500.css"
import "@fontsource/jost/600.css"
import "@fontsource/jost/700.css"

import { useAppDispatch } from './redux/hooks';
import Catalog from './components/catalog/Catalog';
import { HeaderMUI } from './components/header/HeaderMUI';
import { ThemeProvider, createTheme } from '@mui/material/styles';

const NotFound = lazy(() => import("./components/notFound/NotFound"));
const LoginFormMUI = lazy(() => import("./components/loginForm/LoginFormMUI"));
const RegistrationFormMUI = lazy(() => import("./components/registrationForm/RegistrationFormMUI"));
const Cart = lazy(() => import("./components/cart/Cart"));
const Admin = lazy(() => import("./components/admin/admin"));

const theme = createTheme({
  palette: {
    primary: {
      main: '#6F73EE',
    },
    secondary: { main: '#fff' }
  },
  typography: {
    fontFamily: "'Jost', sans-serif"
  }
});

const App: React.FC = () => {

  const dispatch = useAppDispatch()

  useEffect(() => {
    if (localStorage.getItem('token')) {
      dispatch(checkAuth())
    }
  }, [dispatch])


  return (
    <div className="App">
      <ThemeProvider theme={theme}>
        <Router>
          <HeaderMUI />
          <Suspense>
            <Routes>
              <Route path='/' element={<Catalog />} />
              <Route path='/login' element={<LoginFormMUI />} />
              <Route path='/registration' element={<RegistrationFormMUI />} />
              <Route path='/cart' element={<Cart />} />
              <Route path='/admin' element={<Admin />} />

              <Route path='*' element={<NotFound />} />
            </Routes>
          </Suspense>
        </Router>
      </ThemeProvider>
    </div>
  );
}

export default App;
