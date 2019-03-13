import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import { createPublicHoliday } from '../store/actions/publicHolidayActions';

const validationSchema = Yup.object().shape({
  name: Yup.string().required(),
  date: Yup.string().required()
});

class CreatePublicHoliday extends Component {
  render() {
    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={{
            name: '',
            date: ''
          }}
          onSubmit={this.props.createPublicHoliday}
          enableReinitialize
        >
          {({ handleSubmit, handleChange, values, errors, touched }) => (
            <React.Fragment>
              <input
                className={`${
                  errors.name && touched.name ? 'border-danger' : ''
                } mb-3 border`}
                name="name"
                onChange={handleChange}
                defaultValue={values.name}
                placeholder="Public holiday name"
              />
              <input
                className={`${
                  errors.date && touched.date ? 'border-danger' : ''
                } mb-3 border`}
                name="date"
                onChange={handleChange}
                defaultValue={values.date}
                placeholder="Public holiday date"
              />

              <button className="btn" onClick={handleSubmit} type="submit">
                Create
              </button>
            </React.Fragment>
          )}
        </Formik>
      </div>
    );
  }
}

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createPublicHoliday }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(withResetOnNavigate()(CreatePublicHoliday));
