import React, { useEffect, useRef, useState } from 'react';
import '../Css/MainContents.css';
import { Link } from 'react-router-dom';
import axios from 'axios';

function MainContents({ selectedKeyword, searchValue }) {
  const [conimages, setConimages] = useState([]);
  const [visibleImages, setVisibleImages] = useState(8); // 초기에 보여지는 이미지 수
  const imageExtensions = ['png', 'jpg', 'jpeg', 'gif'];

  useEffect(() => {
    const fetchArticles = async () => {
      try {
        const accessToken = localStorage.getItem('accessToken');
        const params = {};

        if (selectedKeyword !== 'Keyword' && searchValue) {
          params.searchType = selectedKeyword.toUpperCase();
          params.searchValue = searchValue;
        }

        const response = await axios.get('/api/articles', {
          params,
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });

        const imageData = response.data.content.filter(item => {
          const fileName = item.storedName;
          const extension = fileName.slice(((fileName.lastIndexOf(".") - 1) >>> 0) + 2);
          return imageExtensions.includes(extension);
        });

        setConimages(imageData);
      } catch (error) {
        console.error(error);
      }
    };

    fetchArticles();
  }, [selectedKeyword, searchValue]);

  const containerRef = useRef(null);

  useEffect(() => {
    const container = containerRef.current;
    const imageElems = container.querySelectorAll('img');

    imageElems.forEach((img, index) => {
      img.style.animationDelay = `${index * 100}ms`;
      img.classList.add('fadeup');
    });
  }, [visibleImages]);

  const handleShowMore = () => {
    setVisibleImages(prevVisibleImages => prevVisibleImages + 8);
  };

  return (
    <div className="MainContents">
      <div className="ContentsContainer" ref={containerRef}>
        {conimages.slice(0, visibleImages).map((image, index) => (
          <div className={`bg${index + 2} image-container`} key={index}>
            <Link to={{
              pathname: `/imgboard`,
              state: { image }
            }}>
              <img 
                src={"https://forshop-bucket.s3.ap-northeast-2.amazonaws.com/" + image.storedName}
                key = {image.id}
                className='imageList'
              />
            </Link>
          </div>
        ))}
      </div>
      {visibleImages < conimages.length && (
        <div className='ButtonContainer'>
          <button className="ShowMoreButton" onClick={handleShowMore}>
            더 보기
          </button>
        </div>
      )}
    </div>
  );
}

export default MainContents;
