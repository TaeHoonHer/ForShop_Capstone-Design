import '../Css/Banner.css'
import React, { useState } from 'react';

function Banner({ selectedKeyword, setSelectedKeyword, setSearchValue}) {
    const [keyword, setKeyword] = useState('Keyword');

    const handleSelectChange = (event) => {
        setSelectedKeyword(event.target.value);
    };

    const handleInputChange = (event) => {
        setSearchValue(event.target.value);
    }

    const dropdownStyle = {
        color: '#f386fd',
        fontSize: '12px',
        border: 'none'
    };

    return (
        <div className="Banner">
            <video autoPlay muted loop>
                <source src="../img/banner.mp4" type="video/mp4"></source>
            </video>
            <h2>4Shop</h2>
            <div className='search'>
                <select value={selectedKeyword} onChange={handleSelectChange} style={dropdownStyle} className='scSelect'>
                    <option value="Keyword" style={dropdownStyle}>Keyword</option>
                    <option value="ID" style={dropdownStyle}>ID</option>
                </select>
                <img src="../img/search.png"/>
                <input className='searchIn' placeholder={selectedKeyword + ' 검색'} onChange={handleInputChange}></input>
            </div>
        </div>
    );
}

export default Banner;