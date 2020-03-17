export default function formatISODate(date: Date): string {
  if (date) {
    // adapted script from
    // http://stackoverflow.com/questions/17415579/how-to-iso-8601-format-a-date-with-timezone-offset-in-javascript
    const tzo = -date.getTimezoneOffset();
    const dif = tzo >= 0 ? "+" : "-";
    const pad2 = (num: number): number | string => {
      var norm = Math.abs(Math.floor(num));
      return (norm < 10 ? "0" : "") + norm;
    };
    return (
      date.getFullYear() +
      "-" +
      pad2(date.getMonth() + 1) +
      "-" +
      pad2(date.getDate()) +
      "T" +
      pad2(date.getHours()) +
      ":" +
      pad2(date.getMinutes()) +
      ":" +
      pad2(date.getSeconds()) +
      dif +
      pad2(tzo / 60) +
      pad2(tzo % 60)
    );
  } else {
    return null;
  }
}
