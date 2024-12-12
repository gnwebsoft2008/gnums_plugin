import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class @nameListView extends GetView {
  @nameListView({super.key});

  //region 1.0.0 Controller declaration...
  final DemoController _controller = Get.put(DemoController()); /// Replace [DemoController] with your Controller
  //endregion

  //region 2.0.0 GNW build method...
  @override
  Widget build(BuildContext context) {

    //region 2.0.1 GNW Scaffold...
    return Scaffold(

      //region 2.0.2 GNW App Bar...
      appBar: AppBar(
        title: Text("List View"),
        backgroundColor: Colors.blueAccent,
      ),
      //endregion

      //region 2.0.3 GNW Body...
      body: Column(
        children: [

          //region 2.0.4 GNW Search Bar...
          CupertinoSearchTextField(),
          //endregion

          //region 2.0.5 GNW List View...
          Expanded(
            child: Scrollbar(
              child: ListView.builder(
                itemCount: 100, /// Put the count of data.
                physics: BouncingScrollPhysics(), /// Apply physics for scroll.
                itemBuilder: (context, index) {
                  return Text(index.toString()); /// Return widget you want to display on list view.
                },
              ),
            ),
          ),
          //endregion

        ],
      ),
      //endregion

    );
    //endregion

  }
  //endregion

}

