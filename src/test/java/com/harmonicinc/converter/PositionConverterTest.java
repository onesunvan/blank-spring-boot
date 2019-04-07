package com.harmonicinc.converter;

import org.junit.Test;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class PositionConverterTest {
    static class A1 {
        String name;
        B1 b1;
        List<E1> e1s;

        public List<E1> getE1s() {
            return e1s;
        }

        public void setE1s(List<E1> e1s) {
            this.e1s = e1s;
        }

        public B1 getB1() {
            return b1;
        }

        public void setB1(B1 b1) {
            this.b1 = b1;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class B1 {
        String text;
        Integer i;

        public Integer getI() {
            return i;
        }

        public void setI(Integer i) {
            this.i = i;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    enum E1 {
        E_1, E_2, E_3, E_EX_1, E_EX_2
    }

    enum E2 {
        E_1, E_2, E_3
    }


    static class A2 {
        String title;
        B2 b2;
        List<E2> es;

        public List<E2> getEs() {
            return es;
        }

        public void setEs(List<E2> es) {
            this.es = es;
        }

        public B2 getB2() {
            return b2;
        }

        public void setB2(B2 b2) {
            this.b2 = b2;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            A2 a2 = (A2) o;
            return Objects.equals(title, a2.title) &&
                    Objects.equals(b2, a2.b2) &&
                    Objects.equals(es, a2.es);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, b2, es);
        }

        @Override
        public String toString() {
            return "A2{" +
                    "title='" + title + '\'' +
                    ", b2=" + b2 +
                    ", e1s=" + es +
                    '}';
        }
    }

    static class B2 {
        Integer textLength;
        Integer i;

        public Integer getI() {
            return i;
        }

        public void setI(Integer i) {
            this.i = i;
        }

        public Integer getTextLength() {
            return textLength;
        }

        public void setTextLength(Integer textLength) {
            this.textLength = textLength;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            B2 b2 = (B2) o;
            return Objects.equals(textLength, b2.textLength) &&
                    Objects.equals(i, b2.i);
        }

        @Override
        public int hashCode() {
            return Objects.hash(textLength, i);
        }

        @Override
        public String toString() {
            return "B2{" +
                    "textLength=" + textLength +
                    ", i=" + i +
                    '}';
        }
    }

    @Test
    public void convert() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(E1.class, E2.class).setConverter(new AbstractConverter<E1, E2>() {
            @Override
            protected E2 convert(E1 e1) {
                if (e1 == null) {
                    return null;
                }
                switch (e1) {
                    case E_1:
                        return E2.E_1;
                    case E_2:
                        return E2.E_2;
                    default:
                        return E2.E_3;
                }
            }
        });

        Converter<String, Integer> converter = mappingContext -> mappingContext.getSource() == null ? null : mappingContext.getSource().length();

        modelMapper.addMappings(new PropertyMap<B1, B2>() {
            @Override
            protected void configure() {
                using(converter).map(source.getText(), destination.getTextLength());
            }
        });



        modelMapper.addMappings(new PropertyMap<A1, A2>() {
            @Override
            protected void configure() {
                map().setTitle(source.getName());
                map(source.getB1(), destination.getB2());
                map(source.getE1s(), destination.getEs());
            }
        });

        B1 b1 = new B1();
        b1.setText("qwertyuiop[]asdfghjk");
        b1.setI(2);

        A1 a1 = new A1();
        a1.setName("asdfdsfdf");
        a1.setB1(b1);
        a1.setE1s(Collections.singletonList(E1.E_EX_1));

        B2 b2Expected = new B2();
        b2Expected.setTextLength(20);
        b2Expected.setI(2);
        A2 expected = new A2();
        expected.setB2(b2Expected);
        expected.setTitle("asdfdsfdf");
        expected.setEs(Collections.singletonList(E2.E_3));

        A2 actual = modelMapper.map(a1, A2.class);


        assertEquals(expected, actual);
        modelMapper.validate();


    }
}