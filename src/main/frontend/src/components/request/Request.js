import React, { Component } from 'react';
import Backdrop from '../popup/Backdrop';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { closeRequestPopup } from '../../store/actions/requestActions';
import { getRequest } from '../../store/selectors';
import Select from '../common/Select';
import BonusIcon from '../../img/icons/bonus_ss.png';
import LeaveIcon from '../../img/icons/leave_ss.png';
import SicknessIcon from '../../img/icons/sickness_ss.png';

const RequestWrapper = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  box-sizing: content-box;
`;

const ButtonsWrapper = styled.div`
  display: flex;
  padding: 0px 100px 0px 100px;
  flex-direction: row;
  justify-content: space-between;
`;

const LeaveWrapper = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  margin: 20px;

  p {
    padding: 10px;
  }
`;

const IconWrapper = styled.div`
  border: 1px solid black;
  padding: 10px;
  transition: ease-in 100ms;
  &:hover {
    transform: scale(1.1);
  }
`;

class Request extends Component {
  mapLeaveTypesForSelect = this.props.leaveTypes.map(type => ({
    value: type.id,
    label: type.name
  }));

  // TODO: Apply react-transition-group for popup enter and popup leave

  render() {
    return (
      <Backdrop title="New Request" closePopup={this.props.closeRequestPopup}>
        <RequestWrapper>
          <ButtonsWrapper>
            <LeaveWrapper>
              <IconWrapper>
                {/* TODO vrackovic: Remove this icons when designed icons are ready (screenshots currently applied) */}
                <img src={LeaveIcon} alt="leave_icon" />
              </IconWrapper>
              <p>Leave</p>
            </LeaveWrapper>
            <LeaveWrapper>
              <IconWrapper>
                <img src={BonusIcon} alt="leave_icon" />
              </IconWrapper>
              <p>Bonus</p>
            </LeaveWrapper>
            <LeaveWrapper>
              <IconWrapper>
                <img src={SicknessIcon} alt="leave_icon" />
              </IconWrapper>
              <p>Sickness</p>
            </LeaveWrapper>
          </ButtonsWrapper>
          {/* {this.props.leaveTypes && (
            <Select options={this.mapLeaveTypesForSelect} />
          )} */}
        </RequestWrapper>
      </Backdrop>
    );
  }
}

const mapStateToProps = state => ({
  request: getRequest(state)
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
