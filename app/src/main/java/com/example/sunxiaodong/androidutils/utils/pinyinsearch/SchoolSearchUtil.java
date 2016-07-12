package com.example.sunxiaodong.androidutils.utils.pinyinsearch;

/**
 * 学校搜索工具
 * Created by sunxiaodong on 16/6/1.
 */
//public class SchoolSearchUtil {
//
//    public static GetSchoolBean search(String searchContent, List<GetSchoolBean> allSchools) {
//        GetSchoolBean school = null;//搜索学校结果
//        if (TextUtils.isEmpty(searchContent)) {
//            return school;
//        }
//        if (ChineseUtil.isChinese(searchContent)) {
//            //如果是中文,则使用全拼查找
//            List<GetSchoolBean> resultSchools = fullPinyinSearch(searchContent, allSchools);
//            if (!resultSchools.isEmpty()) {
//                school = resultSchools.get(0);
//            }
//        } else {
//            //执行首字母查找，字母顺序查找
//            List<GetSchoolBean> resultSchools = letterSearch(searchContent, allSchools);
//            if (!resultSchools.isEmpty()) {
//                school = resultSchools.get(0);
//            }
//        }
//        return school;
//    }
//
//    /**
//     * 全拼搜索
//     * @param searchContent
//     * @param allSchools
//     * @return
//     */
//    private static List<GetSchoolBean> fullPinyinSearch(String searchContent, List<GetSchoolBean> allSchools) {
//        List<GetSchoolBean> schools = new ArrayList<>();
//        String searchContentPinyin = PinYin.getPinYin(searchContent);
//        for (GetSchoolBean schoolBean : allSchools) {
//            Pattern pattern2 = Pattern.compile(searchContentPinyin, Pattern.CASE_INSENSITIVE);
//            Matcher matcher2 = pattern2.matcher(schoolBean.getFullPinYin());
//            if (matcher2.find()) {
//                schools.add(schoolBean);
//            }
//        }
//        return schools;
//    }
//
//    /**
//     * 字符搜索
//     * @param searchContent
//     * @param allSchools
//     * @return
//     */
//    private static List<GetSchoolBean> letterSearch(String searchContent, final List<GetSchoolBean> allSchools) {
//        int schoolNameFullPinyinMaxLength = -1;
//        int searchContentLength = searchContent.length();
//        char firstLetter = searchContent.charAt(0);//首字母
//        String pattern = "" + firstLetter;
//        for (int i = 1; i < searchContentLength; i++) {
//            pattern += "\\w*" + searchContent.charAt(i);
//        }
//        //全拼字母顺序搜索
//        List<GetSchoolBean> allPyLetterOrderSearchResultSchools = new ArrayList<>();
//        for (GetSchoolBean schoolBean : allSchools) {
//            Pattern pattern2 = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
//            Matcher matcher2 = pattern2.matcher(schoolBean.getFullPinYin());
//            if (matcher2.find()) {
//                allPyLetterOrderSearchResultSchools.add(schoolBean);
//                int schoolNameFullPyLength = schoolBean.getFullPinYin().length();
//                if (schoolNameFullPyLength > schoolNameFullPinyinMaxLength) {
//                    schoolNameFullPinyinMaxLength = schoolNameFullPyLength;
//                }
//            }
//        }
//
//        if (allPyLetterOrderSearchResultSchools.isEmpty()) {
//            return allPyLetterOrderSearchResultSchools;
//        }
//
//        //学校名称拼音的首字母顺序搜索
//        int schoolNameFirstLettersMaxLength = -1;
//        List<GetSchoolBean> firstLettersOrderSearchResultSchools = new ArrayList<>();
//        for (GetSchoolBean schoolBean : allPyLetterOrderSearchResultSchools) {
//            Pattern pattern2 = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
//            Matcher matcher2 = pattern2.matcher(schoolBean.getFirstLettersPinYin());
//            if (matcher2.find()) {
//                firstLettersOrderSearchResultSchools.add(schoolBean);
//                int schoolNameLength = schoolBean.getFirstLettersPinYin().length();
//                if (schoolNameLength > schoolNameFirstLettersMaxLength) {
//                    schoolNameFirstLettersMaxLength = schoolNameLength;
//                }
//            }
//        }
//
//        if (firstLettersOrderSearchResultSchools.isEmpty()) {
//            List<GetSchoolBean> allPyLetterOrderSearchSchools = allPyLetterOrderSearch(allPyLetterOrderSearchResultSchools, 0, schoolNameFullPinyinMaxLength, searchContent);
//            return allPyLetterOrderSearchSchools;
//        } else {
//            List<GetSchoolBean> firstLettersOrderSearchSchools = firstLettersOrderSearch(firstLettersOrderSearchResultSchools, 0, schoolNameFirstLettersMaxLength, searchContent);
//            return firstLettersOrderSearchSchools;
//        }
//    }
//
//    /**
//     * 全拼字母顺序搜索
//     * @param leftSchools
//     * @param dealedLength
//     * @param leftLength
//     * @param currLetters
//     * @return
//     */
//    private static List<GetSchoolBean> allPyLetterOrderSearch(List<GetSchoolBean> leftSchools, int dealedLength, int leftLength, String currLetters) {
//        int currLettersLength = currLetters.length();
//        if (leftSchools.isEmpty() || leftLength == 0 || currLettersLength == 0) {
//            return leftSchools;
//        }
//        String resultLetters = "";
//        int resultLeftMaxLength = -1;
//        List<GetSchoolBean> resultSchools = new ArrayList<>();
//
//        boolean finded = false;
//        char currLetter = currLetters.charAt(0);
//        for (int i = 0; i < leftLength; i++) {
//            String pattern = "^" + "\\w" + "{" + i + "}" + currLetter;
//            for (GetSchoolBean schoolBean : leftSchools) {
//                String schoolNameFullPinyin = schoolBean.getFullPinYin();
//                int schoolNameFullPinyinLength = schoolNameFullPinyin.length();
//                String subString = "";
//                if (schoolNameFullPinyinLength > dealedLength) {
//                    subString = schoolNameFullPinyin.substring(dealedLength, schoolNameFullPinyinLength);
//                }
//                Pattern pattern2 = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
//                Matcher matcher2 = pattern2.matcher(subString);
//                if (matcher2.find()) {
//                    resultSchools.add(schoolBean);
//                    finded = true;
//                    int leftSchoolNameLength = schoolNameFullPinyinLength - dealedLength - i - 1;
//                    if (leftSchoolNameLength > resultLeftMaxLength) {
//                        resultLeftMaxLength = leftSchoolNameLength;
//                    }
//                }
//            }
//            if (finded) {
//                dealedLength += (i + 1);//增大处理长度
//                if (currLettersLength > 1) {
//                    resultLetters = currLetters.substring(1, currLettersLength);
//                }
//                break;
//            }
//        }
//        return allPyLetterOrderSearch(resultSchools, dealedLength, resultLeftMaxLength, resultLetters);
//    }
//
//    /**
//     * 学校名称拼音的首字母顺序搜索
//     * @param leftSchools
//     * @param dealedLength
//     * @param leftLength
//     * @param currLetters
//     * @return
//     */
//    private static List<GetSchoolBean> firstLettersOrderSearch(List<GetSchoolBean> leftSchools, int dealedLength, int leftLength, String currLetters) {
//        int currLettersLength = currLetters.length();
//        if (leftSchools.isEmpty() || leftLength == 0 || currLettersLength == 0) {
//            return leftSchools;
//        }
//        String resultLetters = "";
//        int resultLeftMaxLength = -1;
//        List<GetSchoolBean> resultSchools = new ArrayList<>();
//
//        boolean finded = false;
//        char currLetter = currLetters.charAt(0);
//        for (int i = 0; i < leftLength; i++) {
//            String pattern = "^" + "\\w" + "{" + i + "}" + currLetter;
//            for (GetSchoolBean schoolBean : leftSchools) {
//                String firstLetters = schoolBean.getFirstLettersPinYin();// 获得首字母字符串
//                int firstLettersLength = firstLetters.length();
//                String subString = "";
//                if (firstLettersLength > dealedLength) {
//                    subString = firstLetters.substring(dealedLength, firstLettersLength);
//                }
//                Pattern pattern2 = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
//                Matcher matcher2 = pattern2.matcher(subString);
//                if (matcher2.find()) {
//                    resultSchools.add(schoolBean);
//                    finded = true;
//                    int leftSchoolNameFirstLettersLength = firstLettersLength - dealedLength - i - 1;
//                    if (leftSchoolNameFirstLettersLength > resultLeftMaxLength) {
//                        resultLeftMaxLength = leftSchoolNameFirstLettersLength;
//                    }
//                }
//            }
//            if (finded) {
//                dealedLength += (i + 1);//增大处理长度
//                if (currLettersLength > 1) {
//                    resultLetters = currLetters.substring(1, currLettersLength);
//                }
//                break;
//            }
//        }
//        return firstLettersOrderSearch(resultSchools, dealedLength, resultLeftMaxLength, resultLetters);
//    }
//
//}
