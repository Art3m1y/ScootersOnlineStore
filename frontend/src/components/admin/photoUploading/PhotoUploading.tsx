import React, { useState, useEffect } from 'react'

import { CardMedia, Stack, Button } from '@mui/material'



export const PhotoUploading: React.FC<{}> = () => {
    const [selectedFile, setSelectedFile] = useState()
    const [preview, setPreview] = useState<string>()

    // create a preview as a side effect, whenever selected file is changed
    useEffect(() => {
        if (!selectedFile) {
            setPreview(undefined)
            return
        }
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
        <>
            <CardMedia
                sx={{ height: 175, width: 245, border: 'dashed 3px black' }}
                image={preview}
                title="scooter"
            />
            <Stack alignItems="center">
                <Button sx={{ mt: 2 }} variant="contained" component="label">
                    Upload image
                    <input hidden onChange={onSelectFile} accept="image/*" multiple type="file" />
                </Button>
            </Stack>
        </>
    )
}