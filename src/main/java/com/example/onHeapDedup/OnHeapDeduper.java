package com.example.onHeapDedup;

import com.bits.heap.OnHeapSet;
import com.datatorrent.api.Context;
import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.common.util.BaseOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnHeapDeduper extends BaseOperator {

    private static final int MAX = 50000000;

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
            if(key == 1)
                LOG.info("START 1 : " + System.currentTimeMillis());

            unique.emit(key);

            if(key == MAX) {
                LOG.info("DONE " + MAX + " : " + System.currentTimeMillis());
                throw new ShutdownException();
            }
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
