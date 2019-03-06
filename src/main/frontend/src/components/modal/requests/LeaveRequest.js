import { Formik } from 'formik';
import React, { Component } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import * as Yup from 'yup';
import Button from '../../common/Button';

const validationSchema = Yup.object().shape({
  name: Yup.string().required()
});

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
      <Formik
        validationSchema={validationSchema}
        initialValues={{
          name: '',
          teamApprovers: [],
          users: [],
          sendEmailToTeammatesForBonusRequestEnabled: false,
          sendEmailToTeammatesForSicknessRequestEnabled: false,
          sendEmailToTeammatesForAnnualRequestEnabled: false
        }}
        onSubmit={this.props.createTeam}
        render={({
          handleSubmit,
          handleChange,
          values,
          errors,
          touched,
          setFieldValue
        }) => (
          <div className="d-flex justify-content-between">
            <div className="px-2">
              <div className="d-flex justify-content-between mb-2">
                <label htmlFor="leaveType">Type of Leave</label>
                <select name="leaveType">
                  <option value="test">Annual leave</option>
                </select>
              </div>
              <div className="d-flex justify-content-between mb-2">
                <label htmlFor="startDate">Start date</label>
                <DatePicker
                  selected={this.state.startDate}
                  onChange={this.handleChange}
                  test="test"
                />
              </div>
              {/* <div className="d-flex justify-content-between mb-2">
            <label htmlFor="endDate">End date</label>
            <DatePicker
              selected={this.state.endDate}
              onChange={this.handleChange}
            />
          </div> */}
            </div>
            <div className="px-2 d-flex flex-column">
              <textarea className="mb-3" name="" id="" rows="10" />
              <div className="d-flex justify-content-end">
                <Button className="btn btn-danger" title="Apply Leave" />
              </div>
            </div>
          </div>
        )}
      />
    );
  }
}

export default LeaveRequest;
