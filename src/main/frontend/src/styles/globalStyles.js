import { injectGlobal } from 'styled-components';

injectGlobal`
  @import url('https://fonts.googleapis.com/css?family=Montserrat');

*{
  user-select: none;	
}	

html, body {
  margin: 0;
  padding: 0;
  display: block;
  width: 100%;
  height: 100%;
}


body {	
  font-family: 'Montserrat', sans-serif;
  -webkit-font-smoothing: antialiased;
}

#root {
  width: 100%;
  height: 100%;
}

.root-wrapper {
  display: flex;
  flex-direction: row;
  height: 100%;
  width: 100%;
  overflow: hidden;
}

a {	
  text-decoration: none;	
  display: block;
  transition: 0.2s ease-in-out;
  &:hover,	
  &:focus,	
  &:active {	
    text-decoration: none;	
  }	
  &.active {
    background: #E45052;
  }
}	
	
button {	
  border: none;	
  &:hover,	
  &:focus,	
  &:active {	
    outline: none;	
  }	
}	
	
img {	
  max-width: 100%;	
}
	
textarea {	
  resize: none;	
  overflow-y: hidden;	
  &:focus,	
  &:active {	
    outline: none;	
  }	
}

/* Styles for react-datepicker library */
.react-datepicker__month {
  .react-datepicker__week {
    .react-datepicker__day:hover {
      color: #cccccc;
      background-color: #e45052;
      opacity: 0.4;
    }
    .react-datepicker__day--selected, .react-datepicker__day--in-selecting-range, .react-datepicker__day--in-range, .react-datepicker__day--range-start {
      background-color: #e45052;
    }
    .react-datepicker__day--in-range {
      color: #ffffff;
      background-color: #e45052;
      opacity: 0.7;
    }

    .react-datepicker__day--today {
      background-color: lightgrey;
      border-radius: 5px;
      &.react-datepicker__day--in-range {
        background-color: #e45052;
      }
    }
    .react-datepicker__day--range-start, .react-datepicker__day--range-end {
      border-radius: 5px;
      color: #ffffff;
    }
  }
}
`;
