import React, { useEffect, memo, useState } from 'react'
import { Box, Alert } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { DataGrid, GridColumns, GridActionsCellItem, GridToolbar } from '@mui/x-data-grid';
import { useAppDispatch, useAppSelector } from '../../../redux/hooks';
import { Scooter } from '../../../redux/scootersCatalogReducer';
import { getScootersForAdmin, deleteScooterFromAdmin } from '../../../redux/adminReducer';
import { ProductEditModal } from '../editModal/ProductEditModal';


export const ProductGrid: React.FC<{}> = memo(() => {
    const [openProductEdit, setOpenProductEdit] = useState(false);
    const handleOpen = () => setOpenProductEdit(true);
    const handleClose = () => setOpenProductEdit(false);
    const [editingProduct, setEditingProduct] = useState<Scooter>();

    let role = useAppSelector(state => state.auth.role)
    const dispatch = useAppDispatch()
    const [pageSize, setPageSize] = React.useState<number>(5);

    useEffect(() => {
        dispatch(getScootersForAdmin())
    }, [dispatch])

    const rows = useAppSelector(state => state.admin.scooters)
    const isLoading = useAppSelector(state => state.admin.loading)
    const columns = React.useMemo<GridColumns<Scooter>>(
        () => [
            { field: 'id', headerName: 'ID', width: 90 },
            {
                field: 'name',
                headerName: 'Product name',
                width: 150,
            },
            {
                field: 'mark',
                headerName: 'Rating',
                type: 'number',
                width: 110,
            },
            {
                field: 'cost',
                headerName: 'Cost',
                width: 150,
                type: 'number',
            },
            {
                field: 'batteryCapacity',
                headerName: 'Battery capacity',
                width: 110,
                type: 'number',
            },
            {
                field: 'power',
                headerName: 'Power',
                width: 100,
                type: 'number',
            },
            {
                field: 'speed',
                headerName: 'Speed',
                width: 100,
                type: 'number',
            },
            {
                field: 'time',
                headerName: 'Time',
                width: 100,
                type: 'number',
            },
            {
                field: 'actions',
                type: 'actions',
                width: 80,
                getActions: (params) => [
                    <GridActionsCellItem
                        icon={<DeleteIcon />}
                        label="Delete"
                        onClick={() => dispatch(deleteScooterFromAdmin(+params.id))}
                    />,
                    <GridActionsCellItem
                        icon={<EditIcon />}
                        label="Edit"
                        onClick={() => {
                            setEditingProduct(params.row)
                            handleOpen()
                        }}
                    />,
                ],
            },

        ], [dispatch])

    return (
        <>
            <Box sx={{ height: '70vh', width: '100%' }}>
                <DataGrid
                    rows={rows}
                    columns={columns}
                    pageSize={pageSize}
                    onPageSizeChange={(newPageSize) => setPageSize(newPageSize)}
                    rowsPerPageOptions={[5, 10, 15, 20, 25, 50, 100, rows.length]}
                    components={{ Toolbar: GridToolbar }}
                    loading={isLoading}
                    checkboxSelection
                    disableSelectionOnClick
                />
                {role !== 'ROLE_ADMIN' &&

                    <Alert sx={{ mt: 3 }} variant="outlined" severity="warning">
                        Read-only because you are not logged in as an administrator
                    </Alert>
                }
            </Box>
            {editingProduct && <ProductEditModal openProductEdit={openProductEdit} handleClose={handleClose} productData={editingProduct} />}
        </>
    )
})