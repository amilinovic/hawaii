import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Modal, ModalBody } from 'reactstrap';
import { bindActionCreators } from 'redux';
import styled from 'styled-components';
import BonusIcon from '../../img/icons/bonus_ss.png';
import LeaveIcon from '../../img/icons/leave_ss.png';
import SicknessIcon from '../../img/icons/sickness_ss.png';
import { resetModalState } from '../../store/actions/modalActions';
import {
  createBonusRequest,
  createLeaveRequest,
  createSicknessRequest
} from '../../store/actions/requestsActions';
import { getModal, getUser } from '../../store/selectors';
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

const deductedLeaveTypes = [
  {
    id: 1,
    name: 'Annual leave - Vacation'
  },
  {
    id: 2,
    name: 'Training & Education'
  }
];

const sicknessLeaveTypes = [
  {
    id: 4,
    name: 'Asthma related'
  },
  {
    id: 5,
    name: 'Chest related'
  },
  {
    id: 6,
    name: 'Dental Problems'
  },
  {
    id: 7,
    name: 'Cold / Influenza'
  },
  {
    id: 8,
    name: 'Fever'
  },
  {
    id: 9,
    name: 'Food poisoing'
  }
];

class RequestModal extends Component {
  state = {
    modal: false,
    isLeave: false,
    isSickness: false,
    isBonus: false
  };

  componentDidUpdate(prevProps) {
    if (prevProps !== this.props && this.props.modal.shouldClose) {
      this.setState(prevState => ({
        modal: !prevState.modal
      }));
      this.props.resetModalState();
    }
  }

  toggle = () => {
    this.setState(prevState => ({
      modal: !prevState.modal
    }));
  };

  resetState = () => {
    this.setState(() => ({
      isLeave: false,
      isSickness: false,
      isBonus: false
    }));
  };

  requestType = leaveType => {
    this.setState(() => ({
      [leaveType]: true
    }));
  };

  render() {
    const checkIfSpecificRequest =
      this.state.isLeave || this.state.isBonus || this.state.isSickness;

    const requestType = this.state.isLeave ? (
      <RequestForm
        user={this.props.user}
        leaveTypes={deductedLeaveTypes}
        requestAction={createLeaveRequest}
      />
    ) : this.state.isSickness ? (
      <RequestForm
        user={this.props.user}
        leaveTypes={sicknessLeaveTypes}
        requestAction={createSicknessRequest}
      />
    ) : this.state.isBonus ? (
      <RequestForm user={this.props.user} requestAction={createBonusRequest} />
    ) : null;

    return (
      <div>
        <button className="btn btn-danger" onClick={this.toggle}>
          + New Request
        </button>
        <Modal
          onClosed={() => this.resetState()}
          isOpen={this.state.modal}
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
            {requestType}
            {!checkIfSpecificRequest && (
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

            {checkIfSpecificRequest && (
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
  user: getUser(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ resetModalState }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RequestModal);
