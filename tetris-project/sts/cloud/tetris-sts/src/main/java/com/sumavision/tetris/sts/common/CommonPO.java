package com.sumavision.tetris.sts.common;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Poemafar on 2019/12/16 13:54
 */
@MappedSuperclass
public class CommonPO<T>{

    private Long id;

    private String uuid = UUID.randomUUID().toString().replaceAll("-", "");

    private Date createTime;

    private Long createOperator;

    private Date updateTime;

    private Long updateOperator;



    private Boolean beDelete = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "UUID")
    public String getUuid(){
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "CREATE_OPERATOR")
    public Long getCreateOperator() {
        return createOperator;
    }

    public void setCreateOperator(Long createOperator){this.createOperator = createOperator;}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "UPDATE_OPERATOR")
    public Long getUpdateOperator() {
        return updateOperator;
    }

    public void setUpdateOperator(Long updateOperator) {
        this.updateOperator = updateOperator;
    }

    @Column
    @Type(type = "yes_no")
    public Boolean getBeDelete() {
        return beDelete;
    }

    public void setBeDelete(Boolean beDelete) {
        this.beDelete = beDelete;
    }

    @SuppressWarnings("unchecked")
    public T deepClone() throws IOException, ClassNotFoundException {
        // 序列化
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(this);

        // 反序列化
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);

        return (T) ois.readObject();
    }

}
