module.exports = {
  roots: ["<rootDir>/src/main/webapp/react-app"],
  transform: {
    "^.+\\.tsx?$": "ts-jest"
  },
  testRegex: "(/__tests__/.*|(\\.|/)(test|spec))\\.tsx?$",
  moduleFileExtensions: ["ts", "tsx", "js", "jsx", "json", "node"],
  snapshotSerializers: ["enzyme-to-json/serializer"],
  setupFilesAfterEnv: ["<rootDir>/src/main/webapp/react-app/setupEnzyme.ts"],
  moduleNameMapper: {
    "office-ui-fabric-react/lib/(.*)$": "office-ui-fabric-react/lib-commonjs/$1",
    "\\.(css|less)$": "identity-obj-proxy"
  }
}