*** Settings ***
Documentation     Test suite to verify functionality of jrobot-remoe-server
Suite Setup       Setup Suite
Suite Teardown    Clean Suite
Library           Collections
Resource          ../karaf_keywords.robot

*** Variables ***

*** Test Cases ***
Library Type Override Test
    [Documentation]    Tests if Keyword overloads works properly
    ${result}    BaseLib.Concat    Concat me    together.
    Should Be Equal As Strings    ${result}    Concat me together.
    ${result}    BaseLib.Concat    Number    5
    Should Be Equal As Strings    ${result}    Number 6
    ${result}    BaseLib.Concat    5    Number
    Should Be Equal As Strings    ${result}    6 Number
    ${result}    BaseLib.Concat    Concat    me    together.
    Should Be Equal As Strings    ${result}    Concat me together.

Library Numeric Type Override Test
    [Documentation]    Tests if Keyword overloads works properly
    ${sum}    BaseLib.Add    5.2    1.3
    Should be equal as numbers    6.5    ${sum}
    ${sum}    BaseLib.Add    5    1
    Should Be Equal As Integers    6    ${sum}

Library Numeric Type KWARGS Test
    [Documentation]    Tests if Keyword KWARGS works properly
    ${sum}    BaseLib.Sub    5.2    b=1.3
    Should be equal as numbers    3.9    ${sum}
    ${sum}    BaseLib.Sub    b=5.2    a=1.3
    Should be equal as numbers    -3.9    ${sum}
    ${sum}    BaseLib.Sub    b=5    a=1
    Should Be Equal As Integers    -4    ${sum}
    ${sum}    BaseLib.Sub    5    b=1
    Should Be Equal As Integers    4    ${sum}

Library Numeric Type KWARGS Override Test
    [Documentation]    Tests if Keyword KWARGS works properly if specified by Annotation
    ${sum}    ArgumentsLib.Sub    5.2    element_2=1.3
    Should be equal as numbers    3.9    ${sum}
    ${sum}    ArgumentsLib.Sub    element_2=5.2    element_1=1.3
    Should be equal as numbers    -3.9    ${sum}
    ${sum}    ArgumentsLib.Sub    element_2=5    element_1=1
    Should Be Equal As Integers    -4    ${sum}
    ${sum}    ArgumentsLib.Sub    5    element_2=1
    Should Be Equal As Integers    4    ${sum}

Library Inheritance Test
    [Documentation]    Tests if Class inheritance of keywordds works properly
    ${name_1}    BaseLib.Get Name
    ${name_2}    ExtendedLib.Get Name
    Should Not Be Equal As Strings    ${name_1}    ${name_2}
    Should Be Equal As Strings    ${name_1}    Base Library
    Should Be Equal As Strings    ${name_2}    Extended Library
    ${pi}    ExtendedLib.Get Pi
    Should be equal as numbers    3.14    ${pi}

Library Collection Serialization
    [Documentation]    Tests if Collections like Lists, Arrays, Sets and Maps are recieved corectly
    ${resp}    CollectionsLib.Get Array Ints
    Should Be Equal As Strings    ${resp}    [41, 42, 43, 44, 45]
    ${resp}    CollectionsLib.Get Array Chars
    Should Be Equal As Strings    ${resp}    abcd
    ${resp}    CollectionsLib.Get List Integers
    Should Be Equal As Strings    ${resp}    [1, 2, 3, 4]
    ${resp}    CollectionsLib.Get List Strings
    Should Be Equal As Strings    ${resp}    ['1', '2', '3', '4']
    ${resp}    CollectionsLib.Get Set Integers
    Should Be Equal As Strings    ${resp}    [1, 2, 3, 4]
    ${resp}    CollectionsLib.Get Set Strings
    Should Be Equal As Strings    ${resp}    ['1', '2', '3', '4']
    ${resp}    CollectionsLib.Get Map
    ${value}    Get From Dictionary    ${resp}    5
    Should Be Equal As Strings    ${value}    five
    ${value}    Get From Dictionary    ${resp}    6
    Should Be Equal As Strings    ${value}    six
    ${value}    Get From Dictionary    ${resp}    10
    Should Be Equal As Strings    ${value}    ten

Library POJO And Custom Serialization
    [Documentation]    Tests if Classes with custom serializers are recieved corectly
    ${resp}    SerializersLib.Get Dummy Object
    Should Be Equal As Strings    ${resp}    Dummy Object

    ${resp}    SerializersLib.Get Pojo Object    1    2    POJO Object
    ${content}    Evaluate    json.loads('''${resp}''')    json
    ${name}    Get From Dictionary    ${content}    name
    ${x}    Get From Dictionary    ${content}    x
    ${y}    Get From Dictionary    ${content}    y
    Should Be Equal As Strings    ${name}    POJO Object
    Should Be Equal As Numbers    ${x}    1
    Should Be Equal As Numbers    ${y}    2

    ${resp}    SerializersLib.Get Test Object    1    2    Test Object
    ${content}    Evaluate    json.loads('''${resp}''')    json
    ${name}    Get From Dictionary    ${content}    name
    ${coordinates}    Get From Dictionary    ${content}    coordinates
    Should Be Equal As Strings    ${name}    Test Object
    List Should Contain Value    ${coordinates}    1
    List Should Contain Value    ${coordinates}    2

Library Confict resolving
    [Documentation]    Test if Keyword extraction works properly if Keywords conflicts with other methods
    ${resp}    ConflictLib.Conflict Method    nine five one    951
    Should Be Equal As Strings    ${resp}    [nine five one],[951]
    ${resp}    ConflictLib.Conflict Overloaded Method    nine five one    951    false
    Should Be Equal As Strings    ${resp}    [nine five one],[951],[false]
    ${resp}    ConflictLib.Original Keyword    2.5
    Should Be Equal As Strings    ${resp}    Double 2.5
    ${resp}    ConflictLib.Original Keyword    10    9
    Should Be Equal As Strings    ${resp}    Ints 10 9

*** Keywords ***
Setup Suite
    [Documentation]    Initialize Suite resources
    Start Karaf
    Run On Karaf    log:set DEBUG org.robotframework.remoteserver
    Verify Feature Installed On Karaf    jrobot-remote-server
    Verify Feature Started On Karaf    jrobot-remote-server
    Install Bundle On Karaf    mvn:com.github.aenniw/jrobot-test-library
    Verify Bundle Installed On Karaf    jrobot-test-library
    Start Bundle On Karaf    jrobot-test-library
    Verify Bundle Started On Karaf    jrobot-test-library
    Import Library    Remote    http://localhost:8270/BaseLibrary    WITH NAME    BaseLib
    Import Library    Remote    http://localhost:8270/ExtendedLibrary    WITH NAME    ExtendedLib
    Import Library    Remote    http://localhost:8270/ArgumentsLibrary    WITH NAME    ArgumentsLib
    Import Library    Remote    http://localhost:8270/CollectionsLibrary    WITH NAME    CollectionsLib
    Import Library    Remote    http://localhost:8270/SerializersLibrary    WITH NAME    SerializersLib
    Import Library    Remote    http://localhost:8270/ConflictLibrary    WITH NAME    ConflictLib

Clean Suite
    [Documentation]    Cleans Suites resources
    BaseLib.Library Cleanup
    ExtendedLib.Library Cleanup
    Stop Karaf
