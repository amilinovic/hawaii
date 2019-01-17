import React, { Component } from 'react';
import Backdrop from '../popup/Backdrop';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { closeRequestPopup } from '../../store/actions/requestPopupAction';
import { getRequestPopup } from '../../store/selectors';

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
      <Backdrop title="New Request" closePopup={this.props.closeRequestPopup}>
        <RequestWrapper>Popup</RequestWrapper>
      </Backdrop>
    );
  }
}

const mapStateToProps = state => ({
  requestPopup: getRequestPopup(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      closeRequestPopup
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Request);
