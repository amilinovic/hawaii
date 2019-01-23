import React, { Component } from 'react';
import Backdrop from '../popup/Backdrop';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import {
  closeRequestPopup,
  selectRequestType,
  selectAbsenceType,
  selectStartDate,
  selectEndDate
} from '../../store/actions/requestActions';
import { getRequest } from '../../store/selectors';
import RequestType from './RequestType';
import RequestTypeConstants from './requestTypeConstants';
import RequestDetails from './RequestDetails';
import moment from 'moment';

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
      <RequestDetails
        options={selectedAbsenceTypes.map(type => ({
          value: type.id,
          label: type.name
        }))}
        change={selection =>
          this.props.selectAbsenceType(
            this.props.leaveTypes.find(type => type.id === selection.value)
          )
        }
        startDate={{
          date: this.props.request.startDate,
          changeHandler: this.selectStartDateHandler
        }}
        endDate={{
          date: this.props.request.endDate,
          changeHandler: this.selectEndDateHandler
        }}
      />
    );
  };

  selectStartDateHandler = startDate => this.handleDates({ startDate });
  selectEndDateHandler = endDate => this.handleDates({ endDate });

  handleDates = ({ startDate, endDate }) => {
    const start = startDate || this.props.request.startDate;
    let end = endDate || this.props.request.endDate;

    if (moment(start).isAfter(moment(end))) {
      end = start;
    }

    this.props.selectStartDate(start);
    this.props.selectEndDate(end);
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
      selectAbsenceType,
      selectStartDate,
      selectEndDate
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Request);
