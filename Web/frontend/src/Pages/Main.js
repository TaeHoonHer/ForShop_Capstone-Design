import React from 'react'
import '../Css/Main.css'

import Banner from '../Components/Banner';
import MainHeader from '../Components/MainHeader';
import MainContents from '../Components/MainContents';

function Main() {
    return (
        <div className='Main'>
            <MainHeader/>
            <Banner/>
            <div className='MainBox'>
                <MainContents/>
            </div>
        </div>
    )
}

export default Main;