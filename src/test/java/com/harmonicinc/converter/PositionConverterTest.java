package com.harmonicinc.converter;

import com.sun.javafx.image.IntPixelGetter;
import org.junit.Test;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;

import java.util.Objects;

import static org.junit.Assert.*;

public class PositionConverterTest {
    static class A1 {
        String name;
        B1 b1;

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


    static class A2 {
        String title;
        B2 b2;

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
                    Objects.equals(b2, a2.b2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, b2);
        }

        @Override
        public String toString() {
            return "A2{" +
                    "title='" + title + '\'' +
                    ", b2=" + b2 +
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
            }
        });

        B1 b1 = new B1();
        b1.setText("qwertyuiop[]asdfghjk");
        b1.setI(2);

        A1 a1 = new A1();
        a1.setName("asdfdsfdf");
        a1.setB1(b1);

        B2 b2Expected = new B2();
        b2Expected.setTextLength(20);
        b2Expected.setI(2);
        A2 expected = new A2();
        expected.setB2(b2Expected);
        expected.setTitle("asdfdsfdf");

        A2 actual = modelMapper.map(a1, A2.class);

        assertEquals(expected, actual);
        modelMapper.validate();


    }
}