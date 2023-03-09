import React from 'react'
import { Outlet } from 'react-router-dom'
import { HeaderMUI } from './components/header/HeaderMUI'

export const Layout: React.FC<{}> = () => {
    return (
        <>
            <HeaderMUI />
            <Outlet />
        </>
    )
}