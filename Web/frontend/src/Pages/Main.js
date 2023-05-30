import React, { useState } from 'react'
import '../Css/Main.css'

import Banner from '../Components/Banner';
import MainHeader from '../Components/MainHeader';
import MainContents from '../Components/MainContents';
import Footer from '../Components/Footer';

function Main() {
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
                <MainContents
                    selectedKeyword={selectedKeyword}
                    searchValue={submittedSearchValue}
                />
            </div>
            <Footer/>
        </div>
    )
}

export default Main;