import '../Css/MainContents2.css';
import { useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

function MainContents2({ selectedKeyword, searchValue }) {
    const [convideos, setConvideos] = useState([]);
    const [visibleVideos, setVisibleVideos] = useState(8);
    const videoExtensions = ['mp4', 'avi', 'mkv', 'mov', 'flv', 'wmv'];

    useEffect(() => {
        const fetchAccessToken = async () => {
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

                const videoData = response.data.content.filter(item => {
                    const fileName = item.storedName;
                    const extension = fileName.slice(((fileName.lastIndexOf(".") - 1) >>> 0) + 2);
                    return videoExtensions.includes(extension);
                });
                    
                    setConvideos(videoData);
            } catch (error) {
                console.error(error);
            }
        };

        fetchAccessToken();
    }, [selectedKeyword, searchValue]);

    const containerRef = useRef(null);

    useEffect(() => {
        const container = containerRef.current;
        const videoElems = container.querySelectorAll('video');

        videoElems.forEach((video, index) => {
            video.style.animationDelay = `${index * 100}ms`;
            video.classList.add('fadeup');
        });
    }, [visibleVideos]);

    const handleShowMore = () => {
        setVisibleVideos(prevVisibleVideos => prevVisibleVideos + 8);
    };

    return (
        <div className='MainContents'>
            <div className='ContentsContainer' ref={ containerRef }>
                {convideos.slice(0, visibleVideos).map((video, index) => (
                  <div className={`bg${index + 2}`} key={index}>
                    <Link to={{
                        pathname: `/main/vdboard${video.id}`,
                    }}>
                        <video
                            src={"https://forshop-bucket.s3.ap-northeast-2.amazonaws.com/" + video.storedName}
                            key = {video.id}
                            alt={video.alt} autoPlay loop muted />
                    </Link>
                  </div>
                ))}
            </div>
            {visibleVideos < convideos.length && (
                <div className='ButtonContainer'>
                  <button className="ShowMoreButton" onClick={handleShowMore}>
                    더 보기
                  </button>
                </div>
            )}
        </div>
    )
}

export default MainContents2;