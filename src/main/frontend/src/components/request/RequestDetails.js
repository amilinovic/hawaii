import React from 'react';
import Select from '../common/Select';
import styled from 'styled-components';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import 'react-datepicker/dist/react-datepicker-cssmodules.css';
import moment from 'moment';

class RequestDetails extends React.Component {
  isWeekday = input => {
    const dayOfWeek = moment(input).day();
    return dayOfWeek !== 0 && dayOfWeek !== 6;
  };

  render() {
    const { options, change, startDate, endDate } = this.props;

    return (
      <RequestDetailsContainer>
        <Select options={options} change={change} />
        <DatePickerContainer>
          <SingleDatePickerContainer>
            <p>Start Date:</p>
            <DatePicker
              selected={startDate.date}
              selectsStart
              onChange={startDate.changeHandler}
              startDate={startDate.date}
              endDate={endDate.date}
              filterDate={this.isWeekday}
            />
          </SingleDatePickerContainer>
          <SingleDatePickerContainer>
            <p>End Date:</p>
            <DatePicker
              selected={endDate.date}
              selectsEnd
              startDate={startDate.date}
              endDate={endDate.date}
              onChange={endDate.changeHandler}
              filterDate={this.isWeekday}
            />
          </SingleDatePickerContainer>
        </DatePickerContainer>
      </RequestDetailsContainer>
    );
  }
}

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
  .react-datepicker-wrapper {
    padding-top: 10px;
  }
`;

const SingleDatePickerContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: flex-start;
  padding: 20px;
  .react-datepicker__input-container {
    outline: none;
  }
  input {
    width: 300px;
    min-height: 38px;
    border: 1px solid #b3b3b3;
    border-radius: 4px;
    padding-left: 10px;
    outline: none;
  }
`;

export default RequestDetails;
