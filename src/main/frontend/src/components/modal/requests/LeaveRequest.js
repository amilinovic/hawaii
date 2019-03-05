import React, { Component } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

class LeaveRequest extends Component {
  state = {
    startDate: new Date()
  };

  handleChange = date => {
    this.setState({
      startDate: date
    });
  };

  render() {
    return (
      <div className="d-flex justify-content-between">
        <div>
          <DatePicker
            selected={this.state.startDate}
            onChange={this.handleChange}
          />
        </div>
        <div>
          <textarea name="" id="" rows="10" />
          <button className="btn btn-danger">Next</button>
        </div>
      </div>
    );
  }
}

export default LeaveRequest;
