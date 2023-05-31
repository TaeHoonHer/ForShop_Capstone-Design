import React, { useState } from 'react'
import '../Css/MainVideo.css'

import Banner from '../Components/Banner';
import MainHeader from '../Components/MainHeader';
import MainContents2 from '../Components/MainContents2';
import Footer from '../Components/Footer';

function MainVideo() {
    const [selectedKeyword, setSelectedKeyword] = useState('Keyword');
    const [searchValue, setSearchValue] = useState('');
    const [submittedSearchValue, setSubmittedSearchValue] = useState('');

    return (
        <div className='Main'>
            <MainHeader/>
            <Banner
                selectedKeyword={selectedKeyword}
                setSelectedKeyword={setSelectedKeyword}
                setSearchValue={setSearchValue}
                setSubmittedSearchValue={setSubmittedSearchValue}
            />
            <div className='MainBox'>
                <MainContents2
                    selectedKeyword={selectedKeyword}
                    searchValue={submittedSearchValue}
                />
            </div>
            <Footer/>
        </div>
    )
}

export default MainVideo;