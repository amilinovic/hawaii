import React, { Component } from 'react';
import Backdrop from '../popup/Backdrop';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { closeRequestPopup } from '../../store/actions/requestPopupAction';
import { getRequestPopup } from '../../store/selectors';
import Select from '../common/Select';
import BonusIcon from '../../img/icons/bonus_ss.png';
import LeaveIcon from '../../img/icons/leave_ss.png';
import SicknessIcon from '../../img/icons/sickness_ss.png';

const RequestWrapper = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  width: 50vw;
  height: 100px;
  padding: 20px 40px 20px 40px;
  box-sizing: content-box;
`;

const ButtonsWrapper = styled.div`
  width: 100%;
  display: flex;
  padding: 0px 100px 0px 100px;
  flex-direction: row;
  justify-content: space-between;
`;

const LeaveWrapper = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
`;

const IconWrapper = styled.div`
  border: 1px solid black;
  padding: 10px;
`;

class Request extends Component {
  mapLeaveTypesForSelect = this.props.leaveTypes.map(type => ({
    value: type.id,
    label: type.name
  }));

  render() {
    return (
      <Backdrop title="New Request" closePopup={this.props.closeRequestPopup}>
        <RequestWrapper>
          <ButtonsWrapper>
            <LeaveWrapper>
              <IconWrapper>
                <img src={LeaveIcon} alt="leave_icon" />
              </IconWrapper>
              Leave
            </LeaveWrapper>
            <LeaveWrapper>
              <IconWrapper>
                <img src={BonusIcon} alt="leave_icon" />
              </IconWrapper>
              Bonus
            </LeaveWrapper>
            <LeaveWrapper>
              <IconWrapper>
                <img src={SicknessIcon} alt="leave_icon" />
              </IconWrapper>
              Sickness
            </LeaveWrapper>
          </ButtonsWrapper>
          {this.props.leaveTypes && (
            <Select options={this.mapLeaveTypesForSelect} />
          )}
        </RequestWrapper>
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
