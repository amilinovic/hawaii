import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Modal, ModalBody } from 'reactstrap';
import { bindActionCreators } from 'redux';
import styled from 'styled-components';
import BonusIcon from '../../img/icons/bonus_ss.png';
import LeaveIcon from '../../img/icons/leave_ss.png';
import SicknessIcon from '../../img/icons/sickness_ss.png';
import { closeModal, toggleModal } from '../../store/actions/modalActions';
import {
  createBonusRequest,
  createLeaveRequest,
  createSicknessRequest
} from '../../store/actions/requestsActions';
import {
  getAllowance,
  getLeaveTypes,
  getModal,
  getUser
} from '../../store/selectors';
import RequestForm from './requests/RequestForm';

const ModalHeader = styled.div`
  background: #e45052;
  border-top-right-radius: 5px;
  border-top-left-radius: 5px;
`;

const IconWrapper = styled.div`
  cursor: pointer;
  border: 1px solid black;
  padding: 10px;
  transition: ease-in 100ms;
`;

const LeaveWrapper = styled.div`
  img,
  p {
    transition: opacity ease-in 100ms;
  }
  &:hover {
    img,
    p {
      opacity: 0.4;
    }
  }
`;

class RequestModal extends Component {
  state = {
    // modal: false,
    isLeave: false,
    isSickness: false,
    isBonus: false
  };

  toggle = () => {
    this.props.toggleModal();
  };

  resetState = () => {
    this.setState({
      isLeave: false,
      isSickness: false,
      isBonus: false
    });
  };

  requestType = leaveType => {
    this.setState({
      [leaveType]: true
    });
  };

  filterLeaveTypes = leaveType => {
    return this.props.leaveTypes.filter(
      leave => leave.absenceType === leaveType
    );
  };

  getFormProps = () => {
    if (this.state.isLeave) {
      return {
        leaveTypes: this.filterLeaveTypes('DEDUCTED_LEAVE'),
        requestAction: createLeaveRequest
      };
    } else if (this.state.isSickness) {
      return {
        leaveTypes: this.filterLeaveTypes('SICKNESS'),
        requestAction: createSicknessRequest
      };
    } else {
      return {
        leaveTypes: this.filterLeaveTypes('BONUS_DAYS'),
        requestAction: createBonusRequest
      };
    }
  };

  render() {
    const isSpecificRequest =
      this.state.isLeave || this.state.isBonus || this.state.isSickness;

    return (
      <div>
        <Modal
          onClosed={() => this.resetState()}
          isOpen={this.props.modal.open}
          toggle={this.toggle}
          className={this.props.className}
          centered={true}
        >
          <ModalHeader className="d-flex justify-content-between p-3">
            <h3 className="text-white">New Request</h3>
            <span className="text-white" onClick={this.toggle}>
              x
            </span>
          </ModalHeader>
          <ModalBody className="py-4">
            {isSpecificRequest && (
              <RequestForm
                user={this.props.user}
                allowance={this.props.allowance}
                day={this.props.modal.day}
                {...this.getFormProps()}
              />
            )}
            {!isSpecificRequest && (
              <div className="d-flex justify-content-around">
                <LeaveWrapper
                  className="d-flex align-items-center flex-column m-4"
                  onClick={() => this.requestType('isLeave')}
                >
                  <IconWrapper>
                    <img src={LeaveIcon} alt="leave_icon" />
                  </IconWrapper>
                  <p className="p-3">Leave</p>
                </LeaveWrapper>
                <LeaveWrapper
                  className="d-flex align-items-center flex-column m-4"
                  onClick={() => this.requestType('isSickness')}
                >
                  <IconWrapper>
                    <img src={SicknessIcon} alt="sickness_icon" />
                  </IconWrapper>
                  <p className="p-3">Sickness</p>
                </LeaveWrapper>
                <LeaveWrapper
                  className="d-flex align-items-center flex-column m-4"
                  onClick={() => this.requestType('isBonus')}
                >
                  <IconWrapper>
                    <img src={BonusIcon} alt="bonus_icon" />
                  </IconWrapper>
                  <p className="p-3">Bonus</p>
                </LeaveWrapper>
              </div>
            )}

            {isSpecificRequest && (
              <button className="btn" onClick={this.resetState}>
                Back
              </button>
            )}
          </ModalBody>
        </Modal>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  modal: getModal(state),
  user: getUser(state),
  leaveTypes: getLeaveTypes(state),
  allowance: getAllowance(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ closeModal, toggleModal }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RequestModal);
