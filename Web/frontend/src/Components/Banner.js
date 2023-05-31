import '../Css/Banner.css'
import React from 'react';

function Banner({ selectedKeyword, setSelectedKeyword, setSearchValue, setSubmittedSearchValue}) {

    const handleSelectChange = (event) => {
        setSelectedKeyword(event.target.value);
    };

    const handleInputChange = (event) => {
        setSearchValue(event.target.value);
    }

    const handleKeyDown = (event) => {  // 추가된 부분
        if (event.key === 'Enter') {
            setSubmittedSearchValue(event.target.value);
        }
    }

    const dropdownStyle = {
        color: '#f386fd',
        fontSize: '12px',
        border: 'none'
    };
    
    return (
        <div className="Banner">
            <video autoPlay muted loop>
              <source src="/img/banner.mp4" type="video/mp4"></source>
            </video>
            <h2>4Shop</h2>
            <div className='search'>
              <select value={selectedKeyword} onChange={handleSelectChange} style={dropdownStyle} className='scSelect'>
                <option value="NICKNAME" style={dropdownStyle}>Nickname</option>
                <option value="TITLE" style={dropdownStyle}>Title</option>
                <option value="HASHTAG" style={dropdownStyle}>Hashtag</option>
              </select>
              <img src="/img/search.png"/>
              <input className='searchIn' placeholder={selectedKeyword + ' 검색'} onChange={handleInputChange} onKeyDown={handleKeyDown}></input>
            </div>
        </div>
    );
}

export default Banner;
