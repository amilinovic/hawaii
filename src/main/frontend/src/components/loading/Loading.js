import React, { Component } from 'react';
import styled, { keyframes } from 'styled-components';

const loader = keyframes`
0% {
    transform: rotate(0deg);
}

25% {
    transform: rotate(180deg);
}

50% {
    transform: rotate(180deg);
}

75% {
    transform: rotate(360deg);
}

100% {
    transform: rotate(360deg);
}
`;

const inner = keyframes`
0% {
    height: 0%;
}
  
25% {
    height: 0%;
}
  
50% {
    height: 100%;
}
  
75% {
    height: 100%;
}
  
100% {
    height: 0%;
}
`;

const Loader = styled.div`
  width: 30px;
  height: 30px;
  border: 4px solid #e45052;
  animation: ${loader} 2s infinite ease;
`;

const LoaderInner = styled.div`
  background-color: #e45052;
  animation: ${inner} 2s infinite ease-in;
`;

export default class Loading extends Component {
  render() {
    return (
      <div className="d-flex justify-content-center align-items-center flex-grow-1">
        <Loader>
          <LoaderInner />
        </Loader>
      </div>
    );
  }
}
