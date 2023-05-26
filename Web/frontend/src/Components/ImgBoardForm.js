import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import '../Css/ImgBoardForm.css';

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

function ImgBoardForm() {
  const query = useQuery();

  const id = query.get("id");
  const title = query.get("title");
  const src = query.get("src");
  const ct = query.get("content");
  const keyword = query.get("keyword") ? query.get("keyword").split(" ").map(word => `# ${word}`).join(" ") : "";

  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');

  const handleCommentSubmit = (event) => {
    event.preventDefault();
    setComments([...comments, newComment]);
    setNewComment('');
  }

  const handleCommentChange = (event) => {
    setNewComment(event.target.value);
  }

  return (
    <div className='ImgboardWrapper'>
        <div className='boardBox'>
            <div className='imgContainer'>
                <div className='imgBox'>
                    <img src={src} alt={title} />
                </div>
            </div>
            <div className='chatbox'>
                <div className='idBox'>
                    <img src = "/img/user.png"/>
                    <p>{id}</p>
                </div>
                <div className='titleBox'>
                    <h2>{title}</h2>
                    <h3>{keyword}</h3>
                </div>
                <div className='ctBox'>
                    <p>{ct}</p>
                </div>
                <div className='ch'>
                    {comments.map((comment, index) => (
                        <div key={index} className='comment'>
                            <img src="/img/user.png" alt="User" className="commentUserImg" />
                            {comment}
                        </div>
                    ))}
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

export default ImgBoardForm;