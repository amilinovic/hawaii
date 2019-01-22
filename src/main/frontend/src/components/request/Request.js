import React, { Component } from 'react';
import Backdrop from '../popup/Backdrop';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import {
  closeRequestPopup,
  selectRequestType
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
    const selectedAbsenceTypes = this.props.leaveTypes
      .filter(
        type =>
          type.absenceType === this.props.request.requestType
            ? type
            : type.absenceType !== RequestTypeConstants.SICKNESS &&
              type.absenceType !== RequestTypeConstants.BONUS_DAYS
      )
      .map(type => ({
        value: type.id,
        label: type.name
      }));

    return <Select options={selectedAbsenceTypes} />;
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
      selectRequestType
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Request);
