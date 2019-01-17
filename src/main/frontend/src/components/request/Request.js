import React, { Component } from 'react';
import Backdrop from '../popup/Backdrop';
import styled from 'styled-components';

const RequestWrapper = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50vw;
  height: 100px;
`;

class Request extends Component {
  render() {
    return (
      <Backdrop title="New Request">
        <RequestWrapper>Popup</RequestWrapper>
      </Backdrop>
    );
  }
}

export default Request;
