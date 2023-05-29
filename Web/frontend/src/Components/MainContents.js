import React, { useEffect, useRef, useState } from 'react';
import '../Css/MainContents.css';
import { Link } from 'react-router-dom';
import axios from 'axios';

function MainContents({ selectedKeyword, searchValue }) {
  const [conimages, setConimages] = useState([
    
  ]);
  const [visibleImages, setVisibleImages] = useState(8); // 초기에 보여지는 이미지 수
  const [hoveredImage, setHoveredImage] = useState(null);

  useEffect(() => {
    const fetchAccessToken = async () => {
      try {
        const accessToken = localStorage.getItem('accessToken');
        
        const imageResponse = await axios.get('/api/images', {
          headers: {
            'Authorization': `Bearer ${accessToken}` 
          }
        });
        
        const imageExtensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg'];
        const filteredImages = imageResponse.data.filter(image => {
          const extension = image.src.split('.').pop();
          return imageExtensions.includes(extension);
        });
        setConimages(filteredImages);
      } catch (error) {
        console.error(error);
      }
    }

    fetchAccessToken();
  }, []);

  const filteredImages = searchValue === ''
    ? conimages
    : conimages.filter((image) => {
        if (selectedKeyword === 'ID') {
          return String(image.id) === searchValue;
        } else if (selectedKeyword === 'Keyword') {
          return image.keyword.includes(searchValue);
        } else {
          return true;
        }
  });

  const containerRef = useRef(null);

  useEffect(() => {
    const container = containerRef.current;
    const imageElems = container.querySelectorAll('img');

    imageElems.forEach((img, index) => {
      img.style.animationDelay = `${index * 100}ms`;
      img.classList.add('fadeup');
    });
  }, [visibleImages]); // visibleImages 변경 시에만 실행되도록 설정

  const handleShowMore = () => {
    setVisibleImages(prevVisibleImages => prevVisibleImages + 8); // 8개씩 추가로 보여주도록 업데이트
  };

  const handleImageMouseEnter = (image) => {
    setHoveredImage(image);
  };

  const handleImageMouseLeave = () => {
    setHoveredImage(null);
  };

  return (
    <div className="MainContents">
      <div className="ContentsContainer" ref={containerRef}>
        {filteredImages.slice(0, visibleImages).map((image, index) => (
          <div className={`bg${index + 2} image-container`} key={index}>
            <Link to={`/main/imgboard/?id=${image.userId}&title=${image.title}&src=${image.src}&content=${image.content}&keyword=${image.keyword}`}>
              <img 
                src={image.src} 
                alt={image.alt} 
                id='imageList' 
                onMouseEnter={() => handleImageMouseEnter(image)}
                onMouseLeave={handleImageMouseLeave}
              />
            </Link>
            {hoveredImage === image && (
              <div className='imageInfo'>
                <div>{`ID: ${image.id}`}</div>
                <div>{`Title: ${image.title}`}</div>
              </div>
            )}
          </div>
        ))}
      </div>
      {visibleImages < filteredImages.length && (
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