import React, { useEffect } from 'react'
import { TextField, IconButton } from '@mui/material'
import SearchIcon from '@mui/icons-material/Search';
import { setPage, setQuery as setQueryGlobal } from '../../redux/scootersCatalogReducer';
import { useAppDispatch } from '../../redux/hooks';
import useDebounce from '../../customHooks/useDebounce'
import { useSearchParams } from 'react-router-dom';


export const SearchForm: React.FC<{}> = () => {
    const dispatch = useAppDispatch()

    let [searchParams, setSearchParams] = useSearchParams();
    const queryValue = searchParams.get('search') || ''
    const debouncedQuery = useDebounce(queryValue, 700)


    useEffect(() => {
        dispatch(setQueryGlobal(debouncedQuery))
    }, [dispatch, debouncedQuery])

    const onSearchChange = (e: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setSearchParams({ search: e.currentTarget.value })
        dispatch(setPage(1))
    }
    return (
        <>
            <TextField onChange={(e) => { onSearchChange(e) }} value={queryValue} label="Search" size="small" />
            <IconButton type='submit' >
                <SearchIcon />
            </IconButton>
        </>
    )
}