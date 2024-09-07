import React from 'react';
import myImage from '../images/404photo.jpg';
import '../styles/ErrorComponent.css';
import { useEffect } from "react";
import {getErrorCode} from "./api/StudyMateApiService";


export default function ErrorComponent() {
    useEffect(() => {
        getErrorCode().then((response) => {
            console.log(response);
        }
        ).catch((error) => {
            console.error('Error:', error);
        });
    }, []);


    return (
        <div className="ErrorComponent">
            <img src={myImage} className='img' alt="404Image" />
        </div>
    );
}