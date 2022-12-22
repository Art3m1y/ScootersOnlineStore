import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, } from "react-router-dom";
import { checkAuth } from './redux/authReducer';
import "@fontsource/jost/400.css"
import "@fontsource/jost/500.css"
import "@fontsource/jost/600.css"
import "@fontsource/jost/700.css"
import 'bootstrap/dist/css/bootstrap.min.css';
import './app.scss'
import { useAppDispatch } from './redux/hooks';
import Catalog from './components/catalog/Catalog';
import NotFound from './components/notFound/NotFound';
// import RegistrationForm from './components/registrationForm/registrationForm';
// import LoginForm from './components/loginForm/LoginForm';
// import Header from './components/header/Header';
import { HeaderMUI } from './components/header/HeaderMUI';
import { LoginFormMUI } from './components/loginForm/LoginFormMUI';
import { RegistrationFormMUI } from './components/registrationForm/RegistrationFormMUI';
import { ThemeProvider, createTheme } from '@mui/material/styles';


const theme = createTheme({
  palette: {
    primary: {
      main: '#6F73EE',
    },
    secondary:{main: '#fff'}
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
          <Routes>
            <Route path='/' element={<Catalog />} />
            <Route path='/login' element={<LoginFormMUI />} />
            <Route path='/registration' element={<RegistrationFormMUI />} />

            <Route path='*' element={<NotFound />} />
          </Routes>
        </Router>
      </ThemeProvider>;
    </div>
  );
}

export default App;