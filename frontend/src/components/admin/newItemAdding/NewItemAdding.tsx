import React, { useEffect, useState } from 'react'
import { Box, TextField, InputAdornment, CardMedia, Stack, Button, Typography, Alert } from '@mui/material'
import LoadingButton from '@mui/lab/LoadingButton';
import CurrencyRubleIcon from '@mui/icons-material/CurrencyRuble';
import BatteryChargingFullIcon from '@mui/icons-material/BatteryChargingFull';
import BoltIcon from '@mui/icons-material/Bolt';
import SpeedIcon from '@mui/icons-material/Speed';
import TimerOutlinedIcon from '@mui/icons-material/TimerOutlined';
import { useForm, Controller, SubmitHandler } from "react-hook-form"
import { z } from 'zod'
import { zodResolver } from "@hookform/resolvers/zod";
import DownloadIcon from '@mui/icons-material/Download';
import { useAppDispatch, useAppSelector } from '../../../redux/hooks';
import { fetchAddNewItem } from '../../../redux/adminReducer';


const MAX_FILE_SIZE = 524000;
const ACCEPTED_IMAGE_TYPES = ["image/jpeg", "image/jpg", "image/png", "image/webp"];


const formSchema = z.object({
    name: z.string().min(1, { message: "Обязательное поле" }).max(30, { message: 'max length is 30' }),
    cost: z.number().positive().max(2147483647),
    image1: z
        .any()
        .refine((files) => files?.[0]?.size > 0, `Upload image`)
        .refine((files) => files?.[0]?.size <= MAX_FILE_SIZE, `Max image size is 500KB.`)
        .refine(
            (files) => ACCEPTED_IMAGE_TYPES.includes(files?.[0]?.type),
            "Only .jpg, .jpeg, .png and .webp formats are supported."
        ),
    description: z.string().min(1, { message: "Обязательное поле" }).max(400, { message: 'max length is 400' }),
    batteryCapacity: z.number().positive().max(50),
    power: z.number().positive().max(25),
    speed: z.number().positive().max(100),
    time: z.number().positive().max(30),
})
type AddNewItemFormData = z.infer<typeof formSchema>


export const NewItemAdding: React.FC<{}> = () => {
    let role = useAppSelector(state => state.auth.role)

    const dispatch = useAppDispatch()

    const {
        control,
        register,
        formState: {
            errors,
        },
        handleSubmit,
    } = useForm<AddNewItemFormData>({
        defaultValues: {
            name: '',
            cost: 0,
            description: '',
            batteryCapacity: 0,
            power: 0,
            speed: 0,
            time: 0
        },
        resolver: zodResolver(formSchema)
    });

    const onSubmit: SubmitHandler<AddNewItemFormData> = (data) => {
        let product = {
            name: data.name,
            cost: data.cost,
            description: data.description,
            batteryCapacity: data.batteryCapacity,
            speed: data.speed,
            time: data.time,
            power: data.power
        }
        let productData = new Blob([JSON.stringify(product, null, 2)], { type: 'application/json' })
        let formData = new FormData()
        formData.append('product', productData)
        formData.append('image1', data.image1[0])
        console.log(formData)
        dispatch(fetchAddNewItem(formData))
    }


    const [selectedFile, setSelectedFile] = useState<any>()
    const [preview, setPreview] = useState<string>()
    const [selectedFileName, setSelectedFileName] = useState('')

    // create a preview as a side effect, whenever selected file is changed
    useEffect(() => {
        if (!selectedFile) {
            setPreview(undefined)
            return
        }
        setSelectedFileName(selectedFile.name)
        const objectUrl = URL.createObjectURL(selectedFile)
        setPreview(objectUrl)

        // free memory when ever this component is unmounted
        return () => URL.revokeObjectURL(objectUrl)
    }, [selectedFile])

    const onSelectFile = (e: any) => {
        if (!e.target.files || e.target.files.length === 0) {
            setSelectedFile(undefined)
        }
        setSelectedFile(e.target.files[0])
    }

    return (
        <Box component="form" onSubmit={handleSubmit(onSubmit)}>
            <Stack direction={{ xs: 'column', md: 'row' }} spacing={4}>
                <Stack alignItems="center" justifyContent='center'>
                    {preview ?
                        <>
                            <CardMedia
                                component='img'
                                sx={{ height: 245, width: 245, border: 'dashed 3px ', borderColor: 'text.primary' }}
                                image={preview}
                                title="scooter"
                            />
                            <Typography>{selectedFileName}</Typography>
                        </>
                        :
                        <>
                            <DownloadIcon color='primary' sx={{ width: 245, height: 245, border: 'dashed 3px', borderColor: 'text.primary' }} />
                            <Typography>Upload image</Typography>
                        </>
                    }
                    <Stack alignItems="center" justifyContent='center'>
                        <Button sx={{ mt: 2 }} variant="contained" component="label">
                            Upload image
                            <input hidden onChangeCapture={onSelectFile} accept="image/*" multiple type="file" {...register("image1")} />
                        </Button>
                        {errors.image1 &&
                            <Alert sx={{ mt: 2 }} variant="outlined" severity="error">
                                {`${errors.image1?.message}`}
                            </Alert>
                        }
                    </Stack>
                </Stack>
                <Stack sx={{ width: '100%' }}>
                    <Controller
                        name='name'
                        control={control}
                        rules={{ required: true }}
                        render={({ field }) => <TextField {...field}
                            type="text"
                            error={!!errors.name}
                            helperText={errors.name ? errors.name?.message : ''}
                            label="Item name" variant='outlined'
                            fullWidth
                            margin='dense' />}
                    />
                    <Controller
                        name='cost'
                        control={control}
                        render={({ field }) => <TextField {...field}
                            InputProps={{
                                startAdornment: <InputAdornment position="start"><CurrencyRubleIcon /></InputAdornment>,
                            }}
                            onChange={(event) => field.onChange(+event.target.value)}
                            type="number"
                            error={!!errors.cost}
                            helperText={errors.cost ? errors.cost?.message : ''}
                            label="Cost of item" variant='outlined'
                            fullWidth
                            margin='dense' />}
                    />
                    <Controller
                        name='description'
                        control={control}
                        render={({ field }) => <TextField {...field}
                            type="text"
                            error={!!errors.description}
                            helperText={errors.description ? errors.description?.message : ''}
                            label="Item description" variant='outlined'
                            fullWidth
                            margin='dense' />}
                    />
                    <Stack flexDirection='row' justifyContent='space-around' flexWrap='wrap'>
                        <Controller
                            name='batteryCapacity'
                            control={control}
                            render={({ field }) => <TextField {...field}
                                InputProps={{
                                    startAdornment: <InputAdornment position="start"><BatteryChargingFullIcon /></InputAdornment>,
                                }}
                                onChange={(event) => field.onChange(+event.target.value)}
                                type="number"
                                error={!!errors.batteryCapacity}
                                helperText={errors.batteryCapacity ? errors.batteryCapacity?.message : ''}
                                label="Battery capacity" variant='outlined'
                                margin='dense' />}
                        />
                        <Controller
                            name='power'
                            control={control}
                            render={({ field }) => <TextField {...field}
                                InputProps={{
                                    startAdornment: <InputAdornment position="start"><BoltIcon /></InputAdornment>,
                                }}
                                onChange={(event) => field.onChange(+event.target.value)}
                                type="number"
                                error={!!errors.power}
                                helperText={errors.power ? errors.power?.message : ''}
                                label="Power" variant='outlined'
                                margin='dense' />}
                        />
                        <Controller
                            name='speed'
                            control={control}
                            render={({ field }) => <TextField {...field}
                                InputProps={{
                                    startAdornment: <InputAdornment position="start"><SpeedIcon /></InputAdornment>,
                                }}
                                onChange={(event) => field.onChange(+event.target.value)}
                                type="number"
                                error={!!errors.speed}
                                helperText={errors.speed ? errors.speed?.message : ''}
                                label="Speed" variant='outlined'
                                margin='dense' />}
                        />
                        <Controller
                            name='time'
                            control={control}
                            render={({ field }) => <TextField {...field}
                                InputProps={{
                                    startAdornment: <InputAdornment position="start"><TimerOutlinedIcon /></InputAdornment>,
                                }}
                                onChange={(event) => field.onChange(+event.target.value)}
                                type="number"
                                error={!!errors.time}
                                helperText={errors.time ? errors.time?.message : ''}
                                label="Time" variant='outlined'
                                margin='dense' />}
                        />

                    </Stack>
                    <LoadingButton loading={false} type='submit' variant='contained' sx={{ mt: 2, marginLeft: 'auto' }}>Submit</LoadingButton>
                </Stack>
            </Stack>
            {role !== 'ROLE_ADMIN' &&

                <Alert sx={{ mt: 3 }} variant="outlined" severity="warning">
                    Log in as an administrator, otherwise it won't work
                </Alert>
            }
        </Box>
    )
}