import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import '../Css/VdBoardForm.css';
import axios from 'axios';

function VdBoardForm({ videoId }) {
  const [video, setVideo] = useState({});
  const [newComment, setNewComment] = useState('');
  const { articleId } = useParams();

  const navigate = useNavigate();

  useEffect(() => {

    if(!articleId) return;
    const accessToken = localStorage.getItem('accessToken');

    axios.get(`/api/articles/${articleId}`, {
      headers: {
        'Authorization': `Bearer ${accessToken}` 
      }
    }).then(response => {
      setVideo(response.data);
    })
    .catch(error => {
      console.error(error);
    });
  }, [videoId]);

  const handleCommentSubmit = (event) => {
    event.preventDefault();
  
    const accessToken = localStorage.getItem('accessToken');
  
    axios.post('/api/comments/new', {
        articleId: articleId,
        content : newComment 
      }, {
      headers: {
        'Authorization': `Bearer ${accessToken}` 
      }
    }).then(response => {
        if(response.status === 200) {
          // 코멘트가 성공적으로 저장되면 코멘트를 다시 불러옵니다.
          axios.get(`/api/articles/${articleId}`, {
            headers: {
              'Authorization': `Bearer ${accessToken}` 
            }
          }).then(response => {
            setVideo(response.data);
            setNewComment('');  // Comment input field 초기화
          })
          .catch(error => {
            console.error(error);
          });
        }
    })
    .catch(error => {
      console.error(error);
    });
  };

  const handleCommentChange = (event) => {
    setNewComment(event.target.value);
  };

  if (!articleId) {
    navigate("/main");
  };

  return (
    <div className='VdboardWrapper'>
        <div className='boardBox'>
            <div className='vdContainer'>
              <div className='vdBox'>
                <video src={"https://forshop-bucket.s3.ap-northeast-2.amazonaws.com/" + video.storedName} alt={video.title} autoPlay muted/>
              </div>
            </div>
            <div className='chatbox'>
                <div className='idBox'>
                    <img src = "/img/user.png"/>
                    <p>{video.nickname}</p>
                </div>
                <div className='titleBox'>
                    <h2>{video.title}</h2>
                </div>
                <div className='ctBox'>
                    <p>{video.content}</p>
                </div>
                <div className='ch'>
                {video.articleCommentResponse ? Object.entries(video.articleCommentResponse).map(([key, comment]) => (
                    <div className='comment' key={key}>
                      <img src="/img/user.png" alt="User" className="commentUserImg" />
                      {comment.nickname}&nbsp;&nbsp;&nbsp;&nbsp;{comment.content}
                    </div>
                    )) : null}
                    <form onSubmit={handleCommentSubmit}>
                        <input 
                        type="text" 
                        value={newComment} 
                        onChange={handleCommentChange} 
                        placeholder="Write a comment..."
                        />
                        <button type="submit">남기기</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
  );
}

export default VdBoardForm;