import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Switch from 'react-switch';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import Loading from '../components/loading/Loading';
import { requestYear, updateYear } from '../store/actions/yearActions';
import { getYear } from '../store/selectors';

const validationSchema = Yup.object().shape({
  year: Yup.string().required()
});

class EditYear extends Component {
  componentDidMount() {
    this.props.requestYear(this.props.match.params.id);
  }

  render() {
    if (!this.props.year) return <Loading />;

    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={this.props.year}
          onSubmit={this.props.updateYear}
          render={({
            handleSubmit,
            handleChange,
            values,
            errors,
            touched,
            setFieldValue
          }) => (
            <React.Fragment>
              <input
                className={`${
                  errors.year && touched.year ? 'border-danger' : ''
                } w-100 mb-3 border`}
                name="year"
                defaultValue={values.year}
                placeholder="Year"
                onChange={handleChange}
              />
              <div className="d-flex justify-content-between mb-3">
                <h5>Active</h5>
                <Switch
                  onChange={() => setFieldValue('active', !values.active)}
                  checked={values.active}
                  name="active"
                />
              </div>
              <button className="btn" onClick={handleSubmit} type="submit">
                Update year
              </button>
            </React.Fragment>
          )}
        />
      </div>
    );
  }
}

const mapStateToProps = state => ({
  year: getYear(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ updateYear, requestYear }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(EditYear));
