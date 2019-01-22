import React, { Component } from 'react';
import Backdrop from '../popup/Backdrop';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import {
  closeRequestPopup,
  selectRequestType,
  selectAbsenceType
} from '../../store/actions/requestActions';
import { getRequest } from '../../store/selectors';
import Select from '../common/Select';
import RequestType from './RequestType';
import RequestTypeConstants from './requestTypeConstants';

const RequestWrapper = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  box-sizing: content-box;
`;

class Request extends Component {
  filterLeaveTypesBySelectedType = () => {
    let selectedAbsenceTypes = [];

    if (
      this.props.request.requestType === RequestTypeConstants.SICKNESS ||
      this.props.request.requestType === RequestTypeConstants.BONUS_DAYS
    ) {
      selectedAbsenceTypes = this.props.leaveTypes.filter(
        type =>
          type.absenceType ===
          RequestTypeConstants[this.props.request.requestType]
      );
    } else {
      selectedAbsenceTypes = this.props.leaveTypes.filter(
        type =>
          type.absenceType !== RequestTypeConstants.SICKNESS &&
          type.absenceType !== RequestTypeConstants.BONUS_DAYS
      );
    }

    return (
      <Select
        options={selectedAbsenceTypes.map(type => ({
          value: type.id,
          label: type.name
        }))}
        change={selection =>
          this.props.selectAbsenceType(
            this.props.leaveTypes.find(type => type.id === selection.value)
          )
        }
      />
    );
  };

  // TODO: Apply react-transition-group for popup enter and popup leave

  render() {
    return (
      <Backdrop title="New Request" closePopup={this.props.closeRequestPopup}>
        <RequestWrapper>
          {this.props.request.requestType ? (
            this.filterLeaveTypesBySelectedType()
          ) : (
            <RequestType selectRequestType={this.props.selectRequestType} />
          )}
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
      closeRequestPopup,
      selectRequestType,
      selectAbsenceType
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Request);
