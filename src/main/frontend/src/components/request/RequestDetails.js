import React from 'react';
import Select from '../common/Select';
import styled from 'styled-components';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import 'react-datepicker/dist/react-datepicker-cssmodules.css';

const RequestDetailsContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
`;

const DatePickerContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
  align-items: center;
`;

class RequestDetails extends React.Component {
  render() {
    const { options, change, startDate, endDate } = this.props;

    return (
      <RequestDetailsContainer>
        <Select options={options} change={change} />
        <DatePickerContainer>
          <DatePicker
            selected={startDate.date}
            selectsStart
            onChange={startDate.changeHandler}
            startDate={startDate.date}
            endDate={endDate.date}
          />
          <DatePicker
            selected={endDate.date}
            selectsEnd
            startDate={startDate.date}
            endDate={endDate.date}
            onChange={endDate.changeHandler}
          />
        </DatePickerContainer>
      </RequestDetailsContainer>
    );
  }
}

export default RequestDetails;
