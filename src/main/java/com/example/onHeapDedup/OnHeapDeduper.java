package com.example.onHeapDedup;

import com.bits.heap.OnHeapSet;
import com.datatorrent.api.Context;
import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.common.util.BaseOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnHeapDeduper extends BaseOperator {

    private static final Logger LOG = LoggerFactory.getLogger(OnHeapDeduper.class);

    public final transient DefaultOutputPort<Integer> unique = new DefaultOutputPort<Integer>();

    public final transient DefaultOutputPort<Integer> duplicate = new DefaultOutputPort<Integer>();

    public final transient DefaultInputPort<Integer> input = new DefaultInputPort<Integer>() {
        @Override
        public void process(Integer tuple) {
            dedup(tuple);
        }
    };


    private OnHeapSet onHeapSet;

    public OnHeapDeduper() {}

    public void dedup(Integer key){
        if(onHeapSet.add(key)){
            unique.emit(key);
        }
        else {
            duplicate.emit(key);
        }
    }


    @Override
    public void setup(Context.OperatorContext context)
    {
        onHeapSet = new OnHeapSet();
    }


}
