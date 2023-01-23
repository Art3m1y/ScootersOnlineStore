import React, { memo } from 'react'

import { NavLink } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Box, IconButton, Badge, } from '@mui/material'
import MenuOpenIcon from '@mui/icons-material/MenuOpen';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';

import { Container } from '@mui/system';
import { UserMenu } from './UserMenu';
import { BurgerMenu } from './BurgerMenu';
import { FindForm } from '../findForm/SearchForm';
import { useAppSelector } from '../../redux/hooks';

export const HeaderMUI: React.FC<{}> = memo(() => {
    let cartCount = useAppSelector(state => state.cart.totalNumber)

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
                    <NavLink to='/'>
                        <Button variant="contained" endIcon={<MenuOpenIcon />} sx={{ mx: 3, display: { xs: 'none', md: 'inline-flex' } }}>
                            Каталог
                        </Button>
                    </NavLink>
                    <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }} >
                        <FindForm />
                    </Box>
                    <NavLink to='/cart'>
                        <IconButton color="primary" aria-label="open shopping cart">
                            <Badge badgeContent={cartCount} max={99} color="error">
                                <ShoppingCartIcon fontSize="large" />
                            </Badge>
                        </IconButton>
                    </NavLink>
                    <UserMenu />
                </Toolbar>
            </Container>
        </AppBar>
    )
})