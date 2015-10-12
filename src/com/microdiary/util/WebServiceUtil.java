/**
 * 
 */
package com.microdiary.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.microdiary.R;

/**
 * Description:
 * <br/>��վ: <a href="http://www.crazyit.org">���Java����</a> 
 * <br/>Copyright (C), 2001-2012, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class WebServiceUtil
{
	// ����Web Service�������ռ�
	static final String SERVICE_NS = "http://WebXml.com.cn/";
	// ����Web Service�ṩ�����URL
	static final String SERVICE_URL = 
		"http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx";

	// ����Զ��Web Service��ȡʡ���б�
	public static List<String> getProvinceList()
	{
		// ���õķ���
		String methodName = "getRegionProvince";
		// ����HttpTransportSE�������
		HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		ht.debug = true;
		// ʹ��SOAP1.1Э�鴴��Envelop����
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
			SoapEnvelope.VER10);
		// ʵ����SoapObject����
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		envelope.bodyOut = soapObject;
		// ������.Net�ṩ��Web Service���ֽϺõļ�����
		envelope.dotNet = true;
		try
		{
			// ����Web Service
			ht.call(SERVICE_NS + methodName, envelope);
			if (envelope.getResponse() != null)
			{
				// ��ȡ��������Ӧ���ص�SOAP��Ϣ
				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject detail = (SoapObject) result.getProperty(methodName
					+ "Result");
				// ������������Ӧ��SOAP��Ϣ��
				return parseProvinceOrCity(detail);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	// ����ʡ�ݻ�ȡ�����б�
	public static List<String> getCityListByProvince(String province)
	{
		// ���õķ���
		String methodName = "getSupportCityString";
		// ����HttpTransportSE�������
		HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		ht.debug = true;
		// ʵ����SoapObject����
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		// ���һ���������
		soapObject.addProperty("theRegionCode", province);
		// ʹ��SOAP1.1Э�鴴��Envelop����
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
			SoapEnvelope.VER10);
		envelope.bodyOut = soapObject;
		// ������.Net�ṩ��Web Service���ֽϺõļ�����
		envelope.dotNet = true;
		try
		{
			// ����Web Service
			ht.call(SERVICE_NS + methodName, envelope);
			if (envelope.getResponse() != null)
			{
				// ��ȡ��������Ӧ���ص�SOAP��Ϣ
				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject detail = (SoapObject) result.getProperty(methodName
					+ "Result");
				// ������������Ӧ��SOAP��Ϣ��
				return parseProvinceOrCity(detail);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static List<String> parseProvinceOrCity(SoapObject detail)
	{
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < detail.getPropertyCount(); i++)
		{
			// ������ÿ��ʡ��
			result.add(detail.getProperty(i).toString().split(",")[0]);
		}
		return result;
	}

	public static SoapObject getWeatherByCity(String cityName)
	{
		String methodName = "getWeather";
		HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
			SoapEnvelope.VER10);
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		soapObject.addProperty("theCityCode", cityName);
		envelope.bodyOut = soapObject;
		// ������.Net�ṩ��Web Service���ֽϺõļ�����
		envelope.dotNet = true;
		try
		{
			ht.call(SERVICE_NS + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			SoapObject detail = (SoapObject) result.getProperty(methodName
				+ "Result");
			return detail;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	// ���߷������÷�������ѷ��ص�����ͼ���ַ�����ת��Ϊ�����ͼƬ��ԴID��
	public static int parseIcon(String strIcon)
	{
		if (strIcon == null)
			return -1;
		if ("0.gif".equals(strIcon))
			return R.drawable.a_0;
		if ("1.gif".equals(strIcon))
			return R.drawable.a_1;
		if ("2.gif".equals(strIcon))
			return R.drawable.a_2;
		if ("3.gif".equals(strIcon))
			return R.drawable.a_3;
		if ("4.gif".equals(strIcon))
			return R.drawable.a_4;
		if ("5.gif".equals(strIcon))
			return R.drawable.a_5;
		if ("6.gif".equals(strIcon))
			return R.drawable.a_6;
		if ("7.gif".equals(strIcon))
			return R.drawable.a_7;
		if ("8.gif".equals(strIcon))
			return R.drawable.a_8;
		if ("9.gif".equals(strIcon))
			return R.drawable.a_9;
		if ("10.gif".equals(strIcon))
			return R.drawable.a_10;
		if ("11.gif".equals(strIcon))
			return R.drawable.a_11;
		if ("12.gif".equals(strIcon))
			return R.drawable.a_12;
		if ("13.gif".equals(strIcon))
			return R.drawable.a_13;
		if ("14.gif".equals(strIcon))
			return R.drawable.a_14;
		if ("15.gif".equals(strIcon))
			return R.drawable.a_15;
		if ("16.gif".equals(strIcon))
			return R.drawable.a_16;
		if ("17.gif".equals(strIcon))
			return R.drawable.a_17;
		if ("18.gif".equals(strIcon))
			return R.drawable.a_18;
		if ("19.gif".equals(strIcon))
			return R.drawable.a_19;
		if ("20.gif".equals(strIcon))
			return R.drawable.a_20;
		if ("21.gif".equals(strIcon))
			return R.drawable.a_21;
		if ("22.gif".equals(strIcon))
			return R.drawable.a_22;
		if ("23.gif".equals(strIcon))
			return R.drawable.a_23;
		if ("24.gif".equals(strIcon))
			return R.drawable.a_24;
		if ("25.gif".equals(strIcon))
			return R.drawable.a_25;
		if ("26.gif".equals(strIcon))
			return R.drawable.a_26;
		if ("27.gif".equals(strIcon))
			return R.drawable.a_27;
		if ("28.gif".equals(strIcon))
			return R.drawable.a_28;
		if ("29.gif".equals(strIcon))
			return R.drawable.a_29;
		if ("30.gif".equals(strIcon))
			return R.drawable.a_30;
		if ("31.gif".equals(strIcon))
			return R.drawable.a_31;
		return 0;
	}
}
