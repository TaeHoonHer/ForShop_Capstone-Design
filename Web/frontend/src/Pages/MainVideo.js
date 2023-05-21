import React from 'react'
import '../Css/MainVideo.css'

import Banner from '../Components/Banner';
import MainHeader from '../Components/MainHeader';
import MainContents2 from '../Components/MainContents2';
import Footer from '../Components/Footer';

function MainVideo() {
    return (
        <div className='Main'>
            <MainHeader/>
            <Banner/>
            <div className='MainBox'>
                <MainContents2/>
            </div>
            <Footer/>
        </div>
    )
}

export default MainVideo;