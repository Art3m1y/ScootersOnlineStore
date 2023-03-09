import React, { memo } from 'react'

import { useNavigate } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Box, IconButton, Badge, } from '@mui/material'
import MenuOpenIcon from '@mui/icons-material/MenuOpen';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import FavoriteIcon from '@mui/icons-material/Favorite';
import { setQuery } from '../../redux/scootersCatalogReducer';
import { Container } from '@mui/system';
import { UserMenu } from './UserMenu';
import { BurgerMenu } from './BurgerMenu';
import { SearchForm } from '../searchForm/SearchForm';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { ThemeSwitch } from './ThemeSwitch'
import { toggleTheme } from '../../redux/appReducer';


export const HeaderMUI: React.FC<{}> = memo(() => {
    const dispatch = useAppDispatch()
    const mod = useAppSelector(store => store.app.theme)
    let cartCount = useAppSelector(state => state.cart.totalNumber)
    let nav = useNavigate()

    const changeTheme = () => {
        dispatch(toggleTheme())
    }

    return (
        <AppBar position='sticky' color='secondary'>
            <Container maxWidth='xl'>
                <Toolbar sx={{ padding: 0 }}>
                    <BurgerMenu />
                    <Typography
                        variant='h4'
                        component='span'
                        sx={{ fontWeight: 'bold', flexGrow: { xs: 1, md: 0 } }}
                    >
                        KUGOO
                    </Typography>

                    <Button onClick={() => {
                        nav('/')
                        dispatch(setQuery(''))
                    }} variant="contained"
                        endIcon={<MenuOpenIcon />}
                        sx={{ mx: 3, display: { xs: 'none', md: 'inline-flex' } }}>
                        Catalog
                    </Button>

                    <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }} >
                        <SearchForm />
                    </Box>

                    <ThemeSwitch sx={{ display: { xs: 'none', md: 'flex' }, m: 1 }}
                        onChange={changeTheme}
                        checked={mod === 'light' ? false : true} />

                    <IconButton
                        onClick={() => nav('/cart')}
                        color="primary"
                        aria-label="open shopping cart"
                    >
                        <Badge badgeContent={cartCount} max={99} color="error">
                            <ShoppingCartIcon fontSize="large" />
                        </Badge>
                    </IconButton>
                    <IconButton
                        onClick={() => nav('/favorites')}
                        color="primary"
                        aria-label="open favorites"
                        sx={{ display: { xs: 'none', md: 'inline-flex' } }}>
                        <FavoriteIcon fontSize="large" />
                    </IconButton>
                    <UserMenu />
                </Toolbar>
            </Container>
        </AppBar>
    )
})